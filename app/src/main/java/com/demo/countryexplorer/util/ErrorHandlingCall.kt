package com.demo.countryexplorer.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * A wrapper class for Retrofit's `Call<T>` to handle network request retries, cancellations, and response validation.
 *
 * This class provides a suspendable method `await()` that can be used to handle network requests asynchronously
 * within coroutines while integrating error handling. It also supports automatic retries on failure up to a maximum
 * retry limit, and properly cancels requests when the coroutine is cancelled.
 *
 * **Key Features:**
 * - Retries failed requests up to a maximum number of attempts.
 * - Cancels the underlying network call if the coroutine is cancelled.
 * - Validates the HTTP response and deserializes the body, throwing errors when necessary.
 *
 * @param T The type of the response body that will be returned by the Retrofit call.
 * @param call The Retrofit `Call<T>` instance to be wrapped and managed by this class.
 */
class ErrorHandlingCall<T : Any>(
    private val call: Call<T>
) {
    companion object {
        private const val MAX_RETRIES = 3
    }

    /**
     * Suspends the current coroutine, enqueues the OkHttp network call, and validates the response.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine is resumed when the network call gets a response or fails.
     *
     * @return The response body that has been deserialized to an object of type <T>
     */
    suspend fun await(): T = validateResponse(sendRequest())

    /**
     * Suspends the current coroutine and enqueues the OkHttp network call with a transformed
     * request. This can be used to inject headers into the request.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine is resumed when the network call gets a response or fails.
     *
     * @return Retrofit response
     */
    suspend fun sendRequest(): Response<T> = try {
        executeCancellableCall(call)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        // Additional exception handling
        else throw e
    }

    /**
     * Suspends the current coroutine and enqueues the given OkHttp network call.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine will be resumed when the network call gets a response or fails.
     *
     * @return Retrofit response
     */
    private suspend fun executeCancellableCall(call: Call<T>): Response<T> = suspendCancellableCoroutine { continuation ->

        var retryCount = 0

        continuation.invokeOnCancellation {
            if (continuation.isCancelled) {
                try {
                    // If the continuation got cancelled, then cancel the network call as well
                    call.cancel()
                } catch (throwable: Throwable) {
                    // Ignore cancel exception
                }
            }
        }

        call.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (continuation.isCancelled) {
                        // If the continuation was cancelled, then ignore the response
                        return
                    }
                    continuation.resume(response)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    if (continuation.isCancelled) {
                        // If the continuation was cancelled, then ignore the failure
                        return
                    }

                    if (retryCount++ < MAX_RETRIES) {
                        call.clone().enqueue(this)
                    } else {
                        continuation.resumeWithException(t)
                    }
                }
            }
        )
    }

    /**
     * Handles any deserialization errors of the response body, service error responses, and other
     * generic HTTP error responses.
     *
     */
    private fun validateResponse(response: Response<T>): T {

        if (response.isSuccessful) {
            return response.body()
                ?: throw NetworkCommunicationException("Unexpected response content")
        } else {
            throw NetworkCommunicationException("Unsuccessful response from ${(call.request() as Request).url()}: ${response.code()} / ${response.message()}")
        }
    }
}

package com.demo.countryexplorer

import com.demo.countryexplorer.util.ErrorHandlingCall
import com.demo.countryexplorer.util.NetworkCommunicationException
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ErrorHandlingCallTest {

    private lateinit var mockCall: Call<String>
    private lateinit var errorHandlingCall: ErrorHandlingCall<String>

    @Before
    fun setUp() {
        mockCall = mock(Call::class.java) as Call<String>
        errorHandlingCall = ErrorHandlingCall(mockCall)
    }

    @Test
    fun `test successful API response`() = runBlocking {
        val response = Response.success("Success Response")
        doAnswer {
            val callback = it.arguments[0] as Callback<String>
            callback.onResponse(mockCall, response)
        }.`when`(mockCall).enqueue(any())

        val result = errorHandlingCall.await()
        assertEquals("Success Response", result)
    }

    @Test
    fun `test API failure with retries`(): Unit = runBlocking {
        doAnswer {
            val callback = it.arguments[0] as Callback<String>
            callback.onFailure(mockCall, RuntimeException("Network Error"))
        }.`when`(mockCall).enqueue(any())

        assertThrows(RuntimeException::class.java) {
            runBlocking { errorHandlingCall.await() }
        }
    }

    @Test
    fun `test API returns unsuccessful response`(): Unit = runBlocking {
        val response = Response.error<String>(
            500,
            ResponseBody.create(mock(MediaType::class.java), "Server Error")
        )

        // Mock the request to avoid NullPointerException
        val mockRequest = mock(Request::class.java)
        `when`(mockRequest.url()).thenReturn(HttpUrl.get("http://localhost"))
        `when`(mockCall.request()).thenReturn(mockRequest)

        doAnswer {
            val callback = it.arguments[0] as Callback<String>
            callback.onResponse(mockCall, response)
        }.`when`(mockCall).enqueue(any())

        assertThrows(NetworkCommunicationException::class.java) {
            runBlocking { errorHandlingCall.await() }
        }
    }
}

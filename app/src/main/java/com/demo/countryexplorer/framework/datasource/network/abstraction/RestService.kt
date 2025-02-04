package com.demo.countryexplorer.framework.datasource.network.abstraction

import com.google.gson.GsonBuilder
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RestService to support networking using retrofit. This take a class type contract for API definition.
 */
abstract class RestService<API : Any>(private val api: Class<API>) {

    val apiInstance: API by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .also { retrofitBuilder ->
                getRetrofitConverterFactory().forEach { retrofitBuilder.addConverterFactory(it) }
            }
            .build()
            .create(api)
    }

    /**
     * Function to be implemented by the actual service layer extended from [RestService] for base HTTP call URL
     * URL for the REST network call. The path of each request will be defined through respective retrofit
     * interface.
     */
    abstract fun getBaseUrl(): String

    private fun getRetrofitConverterFactory(): List<Converter.Factory> = mutableListOf(
        GsonConverterFactory.create(GsonBuilder().setLenient().serializeNulls().create())
    )
}
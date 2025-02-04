package com.demo.countryexplorer.framework.datasource.network.api.countrylist

import com.demo.countryexplorer.framework.datasource.network.model.Country
import retrofit2.http.GET

/**
 * Retrofit api interface to execute network calls.
 */
interface GitCountryListingAPI {

    @GET("/peymano-wmt/$PATH_HASH/raw/$RESOURCE_HASH/countries.json")
    suspend fun fetchCountryListings() : List<Country>

    companion object {
        const val PATH_HASH = "32dcb892b06648910ddd40406e37fdab"
        const val RESOURCE_HASH = "db25946fd77c5873b0303b858e861ce724e0dcd0"
        const val BASE_URL = "https://gist.githubusercontent.com"
    }
}
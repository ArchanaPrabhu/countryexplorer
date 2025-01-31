package com.demo.countryexplorer.api

import com.demo.countryexplorer.api.GitCountryListingAPI.Companion.BASE_URL
import com.demo.countryexplorer.models.Country

/**
 * Service layer that bounds the API to the RestService and initiates network call.
 */
class CountryListingService : RestService<GitCountryListingAPI>(
    api = GitCountryListingAPI::class.java
) {
    override fun getBaseUrl(): String {
        return BASE_URL
    }

    override suspend fun getCountryListing(): List<Country> {
        val response = apiInstance.fetchCountryListings()
        return response.await().toList()
    }
}
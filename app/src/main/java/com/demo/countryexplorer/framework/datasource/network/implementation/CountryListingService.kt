package com.demo.countryexplorer.framework.datasource.network.implementation

import com.demo.countryexplorer.framework.datasource.network.api.countrylist.GitCountryListingAPI
import com.demo.countryexplorer.framework.datasource.network.api.countrylist.GitCountryListingAPI.Companion.BASE_URL
import com.demo.countryexplorer.framework.datasource.network.model.Country
import com.demo.countryexplorer.framework.datasource.network.abstraction.countrylist.CountryServiceInterface
import com.demo.countryexplorer.framework.datasource.network.abstraction.RestService

/**
 * Service layer that bounds the API to the RestService and initiates network call.
 */
class CountryListingService : CountryServiceInterface, RestService<GitCountryListingAPI>(
    api = GitCountryListingAPI::class.java
) {
    override fun getBaseUrl(): String {
        return BASE_URL
    }

    override suspend fun getCountryListing(): List<Country> {
        return apiInstance.fetchCountryListings()
    }
}
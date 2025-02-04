package com.demo.countryexplorer.framework.datasource.network.abstraction.countrylist

import com.demo.countryexplorer.framework.datasource.network.model.Country

/**
 * Class to define country related APIs
 */
interface CountryServiceInterface {
    suspend fun getCountryListing(
    ): List<Country>
}
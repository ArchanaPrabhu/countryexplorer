package com.demo.countryexplorer.api

import com.demo.countryexplorer.models.Country

/**
 * Class to define country related APIs
 */
interface ServiceInterface {
    suspend fun getCountryListing(
    ): List<Country>
}
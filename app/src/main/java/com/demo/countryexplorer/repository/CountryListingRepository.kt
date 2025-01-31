package com.demo.countryexplorer.repository

import com.demo.countryexplorer.models.Country
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for CountryListing
 */
interface CountryListingRepository {
    fun  getCountryListing(): Flow<List<Country>>
}
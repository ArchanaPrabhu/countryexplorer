package com.demo.countryexplorer.repository

import com.demo.countryexplorer.api.CountryListingService
import com.demo.countryexplorer.models.Country
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository implementation for [CountryListingRepository]
 */
@FlowPreview
class CountryListingRepositoryImpl(
    private val countryService: CountryListingService = CountryListingService()
) : CountryListingRepository {

    override fun getCountryListing(): Flow<List<Country>> {
        return flow { emit(countryService.getCountryListing()) }
    }
}
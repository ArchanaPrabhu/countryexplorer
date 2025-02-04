package com.demo.countryexplorer.business.interactors.countrylist

import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.business.domain.state.StateEvent
import com.demo.countryexplorer.business.domain.state.UiState
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for CountryListing
 */
interface CountryListingRepository {
    fun  getCountryListingNew(stateEvent: StateEvent): Flow<UiState<CountryListingViewState>>
}
package com.demo.countryexplorer.business.data.cache.abstraction

import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryStateEvent
import kotlinx.coroutines.flow.Flow

interface CountryListCacheDataSource {
    fun fetchAllCountries(stateEvent: CountryStateEvent) : Flow<UiState<CountryListingViewState>>
}
package com.demo.countryexplorer.business.data.network.implementation

import com.demo.countryexplorer.business.data.network.abstraction.CountryListRemoteDataSource
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryStateEvent
import kotlinx.coroutines.flow.Flow

class CountryListRemoteDataSourceImpl(

) : CountryListRemoteDataSource {
    override fun fetchAllCountries(stateEvent: CountryStateEvent): Flow<UiState<CountryListingViewState>> {
        TODO()
    }
}
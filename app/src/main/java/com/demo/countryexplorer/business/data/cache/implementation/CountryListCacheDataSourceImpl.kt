package com.demo.countryexplorer.business.data.cache.implementation

import com.demo.countryexplorer.business.data.cache.abstraction.CountryListCacheDataSource
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryStateEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryListCacheDataSourceImpl : CountryListCacheDataSource {
    override fun fetchAllCountries(stateEvent: CountryStateEvent): Flow<UiState<CountryListingViewState>> {
        return flow {
            UiState.data(
                response = Response(
                    message = "Cache result",
                    uiComponentType = UIComponentType.Toast(), messageType = MessageType.Info()
                ),
                data = null,
                stateEvent = stateEvent
            )
        }
    }
}
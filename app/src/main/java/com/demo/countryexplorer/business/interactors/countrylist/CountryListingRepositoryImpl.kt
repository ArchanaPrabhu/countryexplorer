package com.demo.countryexplorer.business.interactors.countrylist

import com.demo.countryexplorer.framework.datasource.network.implementation.CountryListingService
import com.demo.countryexplorer.business.data.network.ApiResponseHandler
import com.demo.countryexplorer.business.data.util.safeApiCall
import com.demo.countryexplorer.framework.datasource.network.model.Country
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState.CountryLists
import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.StateEvent
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.business.domain.state.UiState
import kotlinx.coroutines.Dispatchers
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

    override fun getCountryListingNew(stateEvent: StateEvent): Flow<UiState<CountryListingViewState>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) {
                countryService.getCountryListing()
            }
            emit(object : ApiResponseHandler<CountryListingViewState, List<Country>>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: List<Country>): UiState<CountryListingViewState> {
                    return UiState.data(
                        response = Response(
                            message = resultObj.toString(),
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = CountryListingViewState(
                            CountryLists(
                                countryLists = resultObj
                            )
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult())
        }
}
package com.demo.countryexplorer.business.data.cache

import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.StateEvent
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.business.data.cache.CacheErrors.CACHE_DATA_NULL


abstract class CacheResponseHandler <ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): UiState<ViewState> {

        return when(response){

            is CacheResult.GenericError -> {
                UiState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    UiState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${CACHE_DATA_NULL}.",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): UiState<ViewState>

}
package com.demo.countryexplorer.business.data.network

import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.StateEvent
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.demo.countryexplorer.business.data.network.NetworkErrors.NETWORK_ERROR

abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): UiState<ViewState> {
        return when(response){
            is ApiResult.GenericError -> {
                UiState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage.toString()}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                UiState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_ERROR}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    UiState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_DATA_NULL}.",
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
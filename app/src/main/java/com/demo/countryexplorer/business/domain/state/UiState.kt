package com.demo.countryexplorer.business.domain.state

/**
 * Generic class that contains data and status about loading data
 */
data class UiState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var stateEvent: StateEvent? = null
) {

    companion object {

        fun <T> error(
            response: Response,
            stateEvent: StateEvent?
        ): UiState<T> {
            return UiState(
                stateMessage = StateMessage(
                    response
                ),
                data = null,
                stateEvent = stateEvent
            )
        }

        fun <T> data(
            response: Response?,
            data: T? = null,
            stateEvent: StateEvent?
        ): UiState<T> {
            return UiState(
                stateMessage = response?.let {
                    StateMessage(
                        it
                    )
                },
                data = data,
                stateEvent = stateEvent
            )
        }
    }
}

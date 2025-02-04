package com.demo.countryexplorer.framework.presentation.countrylist.state

import com.demo.countryexplorer.business.domain.state.StateEvent

sealed class CountryStateEvent : StateEvent {

    class FetchAllListsStateEvent: CountryStateEvent() {
        override fun errorInfo(): String {
            return "Error fetching for country lists"
        }

        override fun eventName(): String {
            return "FetchAllListsStateEvent"
        }

        override fun shouldDisplayProgressBar() = true

    }

    class SearchForCountryStateEvent: CountryStateEvent() {
        override fun errorInfo(): String {
            return "Unable to find any search item"
        }

        override fun eventName(): String {
            return "FetchAllListsStateEvent"
        }

        override fun shouldDisplayProgressBar() = true

    }
}
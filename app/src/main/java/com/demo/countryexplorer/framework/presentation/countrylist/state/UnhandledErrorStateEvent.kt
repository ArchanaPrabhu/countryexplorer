package com.demo.countryexplorer.framework.presentation.countrylist.state

import com.demo.countryexplorer.business.domain.state.StateEvent

class UnhandledErrorStateEvent(val throwable : Throwable? = null) : StateEvent {
    override fun errorInfo(): String {
        return "Exception with reason: ${throwable?.message?: "Unknown"}"
    }

    override fun eventName(): String {
        return "UnhandledErrorStateEvent"
    }

    override fun shouldDisplayProgressBar(): Boolean {
        return false
    }
}
package com.demo.countryexplorer.ui.state

import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.StateMessageCallback

interface UIController {

    fun displayProgressBar(shouldDisplay: Boolean)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}

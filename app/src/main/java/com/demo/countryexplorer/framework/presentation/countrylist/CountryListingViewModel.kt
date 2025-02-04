package com.demo.countryexplorer.framework.presentation.countrylist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.demo.countryexplorer.business.interactors.countrylist.CountryListingRepository
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryListingViewState.CountryLists
import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryStateEvent
import com.demo.countryexplorer.business.domain.state.StateEvent
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.framework.presentation.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * View Model for the Country Listing
 */
@ExperimentalCoroutinesApi
@FlowPreview
class CountryListingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CountryListingRepository
) : BaseViewModel<CountryListingViewState>() {

    fun saveScrollPosition(position: Int) {
        savedStateHandle[SCROLL_POSITION] = position
    }

    fun getScrollPosition(): Int? {
        return savedStateHandle[SCROLL_POSITION] ?: null
    }

    fun shouldRestoreScroll(): Boolean {
        return savedStateHandle[WAS_ROTATED] ?: false
    }

    fun setRotationFlag() {
        savedStateHandle[WAS_ROTATED] = true
    }

    fun clearRotationFlag() {
        savedStateHandle[WAS_ROTATED] = false
    }

    companion object {
        private const val TAG = "CountryListViewModel"
        const val SCROLL_POSITION = "scroll_position"
        const val WAS_ROTATED = "was_rotated"
    }

    override fun handleNewData(data: CountryListingViewState) {
        data.let { viewState ->
            setCountryListData(viewState.countryLists)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<UiState<CountryListingViewState>> = when (stateEvent) {
            is CountryStateEvent.FetchAllListsStateEvent -> {
                repository.getCountryListingNew(stateEvent)
            }

            else -> {
                flow {
                    emit(
                        UiState.error(
                            response = Response(
                                message = "Something went wrong with the network call",
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        ),
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): CountryListingViewState {
        return CountryListingViewState()
    }

    fun setCountryListData(countryLists: CountryLists) {
        val update = getCurrentViewStateOrNew()
        update.countryLists = countryLists
        setViewState(update)
    }
}

@FlowPreview
internal class CountryListViewModelFactory(
    private val repository: CountryListingRepository
) : ViewModelProvider.Factory {

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        CountryListingViewModel(
            extras.createSavedStateHandle(),
            repository
        ) as T
}

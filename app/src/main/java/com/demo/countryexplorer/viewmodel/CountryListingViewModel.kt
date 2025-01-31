package com.demo.countryexplorer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.demo.countryexplorer.models.Country
import com.demo.countryexplorer.repository.CountryListingRepository
import com.demo.countryexplorer.util.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * View Model for the Country Listing
 */
@FlowPreview
class CountryListingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CountryListingRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val _currentCountryListContentState =
        MutableStateFlow<UiState<List<Country>>>(UiState.Loading)
    val currentCountryListContentState: StateFlow<UiState<List<Country>>> =
        _currentCountryListContentState.asStateFlow()

    private val listExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _currentCountryListContentState.value = UiState.Error(throwable)
        Log.e(TAG, "Error when fetching country list,  ${throwable.message}")
    }

    private val _scrollPosition = savedStateHandle.getLiveData<Int>(SCROLL_POSITION)

    /**
     * Function to fetch country list from the repository layer.
     */
    fun fetchCountryList() {
        viewModelScope.launch(dispatcher + listExceptionHandler) {
            repository.getCountryListing()
                .collect { countryList ->
                    _currentCountryListContentState.value = UiState.Success(countryList)
                }
        }
    }

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    fun getScrollPosition(): Int? {
        return _scrollPosition.value
    }

    fun setScrollRestoredFlag() {
        savedStateHandle[RESTORE] = true
    }

    fun isScrollRestored(): Boolean {
        return savedStateHandle[RESTORE] ?: false
    }

    companion object {
        private const val TAG = "CountryListViewModel"
        const val SCROLL_POSITION = "scroll_position"
        const val RESTORE = "scroll_restored"
    }
}

@FlowPreview
internal class CountryListViewModelFactory(
    private val repository: CountryListingRepository
) : ViewModelProvider.Factory {

    @OptIn(FlowPreview::class)
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        CountryListingViewModel(
            extras.createSavedStateHandle(),
            repository
        ) as T
}

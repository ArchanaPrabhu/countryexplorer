package com.demo.countryexplorer.framework.presentation.countrylist.state

import android.os.Parcelable
import com.demo.countryexplorer.framework.datasource.network.model.Country
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryListingViewState(
    var countryLists: CountryLists = CountryLists(),
    var scrollState: ScrollState = ScrollState()
) : Parcelable {

    @Parcelize
    data class CountryLists(
        var countryLists: List<Country> = mutableListOf(),
        var searchQuery: String? = null
    ) : Parcelable

    @Parcelize
    data class ScrollState(
        var scrollPosition : Int? = 0,
        var shouldScroll : Boolean? = false,
    ) : Parcelable
}

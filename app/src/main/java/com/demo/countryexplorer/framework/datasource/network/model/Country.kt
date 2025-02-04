package com.demo.countryexplorer.framework.datasource.network.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class to define the Country item
 */
@Parcelize
data class Country(
    @Expose
    @SerializedName("capital")
    var capitol: String,
    @Expose
    @SerializedName("code")
    var code: String,
    @Expose
    @SerializedName("currency")
    var currency: Currency,
    @Expose
    @SerializedName("flag")
    var flag: String,
    @Expose
    @SerializedName("language")
    var language: Language,
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("region")
    var region: String
) : Parcelable

@Parcelize
data class Currency(
    @Expose
    @SerializedName("code")
    var code: String,
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("symbol")
    var symbol: String
) : Parcelable

@Parcelize
data class Language(
    @Expose
    @SerializedName("code")
    var code: String,
    @Expose
    @SerializedName("name")
    var name: String
) : Parcelable
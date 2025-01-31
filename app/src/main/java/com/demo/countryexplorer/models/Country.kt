package com.demo.countryexplorer.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Data class to define the Country item
 */
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
)

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
)

data class Language(
    @Expose
    @SerializedName("code")
    var code: String,
    @Expose
    @SerializedName("name")
    var name: String
)
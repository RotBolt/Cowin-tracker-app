package io.rotlabs.cowintracker.data.model


import com.google.gson.annotations.SerializedName

data class VaccineFee(
    @SerializedName("fee")
    val fee: String,
    @SerializedName("vaccine")
    val vaccine: String
)
package io.rotlabs.cowintracker.data.model


import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("available_capacity")
    val availableCapacity: Int,
    @SerializedName("available_capacity_dose1")
    val availableCapacityDose1: Int,
    @SerializedName("available_capacity_dose2")
    val availableCapacityDose2: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("min_age_limit")
    val minAgeLimit: Int,
    @SerializedName("session_id")
    val sessionId: String,
    @SerializedName("slots")
    val slots: List<String>,
    @SerializedName("vaccine")
    val vaccine: String
)
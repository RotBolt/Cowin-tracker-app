package io.rotlabs.cowintracker.data.model


import com.google.gson.annotations.SerializedName

data class VaccineAppointments(
    @SerializedName("centers")
    val centers: List<Center>
)
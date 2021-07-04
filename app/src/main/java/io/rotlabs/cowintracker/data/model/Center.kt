package io.rotlabs.cowintracker.data.model


import com.google.gson.annotations.SerializedName

data class Center(
    @SerializedName("address")
    val address: String,
    @SerializedName("block_name")
    val blockName: String,
    @SerializedName("center_id")
    val centerId: Int,
    @SerializedName("district_name")
    val districtName: String,
    @SerializedName("fee_type")
    val feeType: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("lat")
    val lat: Int,
    @SerializedName("long")
    val long: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pincode")
    val pincode: Int,
    @SerializedName("sessions")
    val sessions: List<Session>,
    @SerializedName("state_name")
    val stateName: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("vaccine_fees")
    val vaccineFees: List<VaccineFee>
)
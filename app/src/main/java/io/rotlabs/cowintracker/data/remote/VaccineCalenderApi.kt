package io.rotlabs.cowintracker.data.remote

import io.rotlabs.cowintracker.data.model.VaccineAppointments
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface VaccineCalenderApi {

    @GET("v2/appointment/sessions/public/calendarByDistrict")
    fun getVaccineAppointmentsByCalendar(
        @Query("district_id") districtId: String,
        @Query("date") date: String
    ): Call<VaccineAppointments>
}
package io.rotlabs.cowintracker.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Networking {

    private const val baseUrl = "https://cdn-api.co-vin.in/api/"

    private var retrofit: Retrofit? = null
    private var vaccineApi: VaccineCalenderApi? = null

    @Synchronized
    private fun getRetrofit(): Retrofit? {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient().newBuilder()
                        .addInterceptor(RequestInterceptor())
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BASIC
                        })
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    @Synchronized
    fun getVaccineCalendarApi(): VaccineCalenderApi {
        if (vaccineApi == null) {
            vaccineApi = getRetrofit()!!.create(VaccineCalenderApi::class.java)
        }
        return vaccineApi!!
    }
}
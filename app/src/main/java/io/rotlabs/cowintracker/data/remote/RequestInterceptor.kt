package io.rotlabs.cowintracker.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.run {
        val newRequestBuilder = request().newBuilder()

        newRequestBuilder.apply {
            addHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:88.0) Gecko/20100101 Firefox/88.0")
        }

        proceed(newRequestBuilder.build())

    }
}
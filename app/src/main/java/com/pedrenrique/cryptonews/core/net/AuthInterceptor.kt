package com.pedrenrique.cryptonews.core.net

import com.pedrenrique.cryptonews.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor  : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("charset", "utf-8")
            .header("X-Api-Key", BuildConfig.NEWS_API_KEY)
            .method(originalRequest.method(), originalRequest.body())

        return chain.proceed(requestBuilder.build())
    }
}
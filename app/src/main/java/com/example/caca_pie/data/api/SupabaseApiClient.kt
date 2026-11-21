package com.example.caca_pie.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseApiClient {
    private const val BASE_URL = "https://hoszntccufatqqguesmr.supabase.co/rest/v1/"
    private const val API_KEY = "sb_publishable_tpXNcyG0yzRnmfMCT6lEgw_hV_csSfn"
    private const val SCHEMA = "mobile"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", API_KEY)
                    .addHeader("Accept-Profile", SCHEMA)
                    .addHeader("Content-Profile", SCHEMA)
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val apiService: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}

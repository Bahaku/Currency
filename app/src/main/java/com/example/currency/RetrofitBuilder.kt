package com.example.currency

import com.example.currency.BuildConfig.BASE_URL
import com.example.currency.data.CurrencyService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private var service: CurrencyService? = null

    fun getService(): CurrencyService? {
        if (service == null)
            service = buildRetrofit()

        return service
    }

    private fun buildRetrofit(): CurrencyService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
    }
}
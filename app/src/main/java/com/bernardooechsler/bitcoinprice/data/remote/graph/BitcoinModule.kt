package com.bernardooechsler.bitcoinprice.data.remote.graph

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BitcoinModule {

    fun createBitcoin(): BitcoinService {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BitcoinService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())

        return retrofit.build().create(BitcoinService::class.java)
    }
}
package com.bernardooechsler.bitcoinprice.data.remote.price

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object BitcoinInfoModule {

    fun createBitcoin(): BitcoinInfoService {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BitcoinInfoService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())

        return retrofit.build().create(BitcoinInfoService::class.java)
    }
}
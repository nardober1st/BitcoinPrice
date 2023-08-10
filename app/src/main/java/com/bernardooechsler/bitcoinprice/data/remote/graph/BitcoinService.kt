package com.bernardooechsler.bitcoinprice.data.remote.graph

import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import retrofit2.Response
import retrofit2.http.GET

interface BitcoinService {

    @GET("charts/market-price?timespan=4weeks&format=json")
    suspend fun getBitcoin(): Response<Bitcoin>

    companion object {
        const val BASE_URL = "https://api.blockchain.info/"
    }
}
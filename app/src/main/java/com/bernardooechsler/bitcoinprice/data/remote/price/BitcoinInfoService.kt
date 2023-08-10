package com.bernardooechsler.bitcoinprice.data.remote.price

import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo
import retrofit2.Response
import retrofit2.http.GET

interface BitcoinInfoService {

    @GET("exchange/tickers/BTC-USD")
    suspend fun getBitcoinInfo(): Response<BitcoinInfo>

    companion object {
        const val BASE_URL = "https://api.blockchain.com/v3/"
    }
}
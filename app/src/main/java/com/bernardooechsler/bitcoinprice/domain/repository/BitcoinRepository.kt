package com.bernardooechsler.bitcoinprice.domain.repository

import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo

interface BitcoinRepository {

    suspend fun getBitcoinData(): Bitcoin?
    suspend fun getBitcoinFromNetwork(): Bitcoin?
    suspend fun getBitcoinInfo(): BitcoinInfo?
    suspend fun getBitcoinInfoFromNetwork(): BitcoinInfo?

    suspend fun insertBitcoin(bitcoin: Bitcoin)
    suspend fun insertBitcoinInfo(bitcoinInfo: BitcoinInfo)

//    suspend fun updateBitcoin(bitcoin: Bitcoin)
//    suspend fun updateBitcoinInfo(bitcoinInfo: BitcoinInfo)
    suspend fun getTodayDataPrice(): DataPrice?
    suspend fun getYesterdayDataPrice(): DataPrice?

}
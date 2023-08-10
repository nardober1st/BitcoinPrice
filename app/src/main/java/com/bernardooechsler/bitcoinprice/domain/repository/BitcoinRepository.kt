package com.bernardooechsler.bitcoinprice.domain.repository

import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo

interface BitcoinRepository {

    suspend fun getBitcoinData(): Bitcoin?
    suspend fun getBitcoinFromNetwork(): Bitcoin?
    suspend fun getBitcoinInfo(): BitcoinInfo?
    suspend fun getBitcoinInfoFromNetwork(): BitcoinInfo?

}
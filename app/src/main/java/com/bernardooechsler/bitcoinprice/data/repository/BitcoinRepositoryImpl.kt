package com.bernardooechsler.bitcoinprice.data.repository

import android.util.Log
import com.bernardooechsler.bitcoinprice.data.local.graph.BitcoinDao
import com.bernardooechsler.bitcoinprice.data.local.graph.DataPriceDao
import com.bernardooechsler.bitcoinprice.data.local.price.BitcoinInfoDao
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo
import com.bernardooechsler.bitcoinprice.data.remote.graph.BitcoinService
import com.bernardooechsler.bitcoinprice.data.remote.price.BitcoinInfoService
import com.bernardooechsler.bitcoinprice.domain.repository.BitcoinRepository

class BitcoinRepositoryImpl(
    private val bitcoinService: BitcoinService,
    private val bitcoinInfoService: BitcoinInfoService,
    private val bitcoinDao: BitcoinDao,
    private val dataPriceDao: DataPriceDao,
    private val bitcoinInfoDao: BitcoinInfoDao
) : BitcoinRepository {

    override suspend fun getBitcoinData(): Bitcoin? {
        try {
            val localBitcoin = bitcoinDao.getLastBitcoin()

            if (localBitcoin != null) {
                return localBitcoin
            } else {
                val response = bitcoinService.getBitcoin()
                if (response.isSuccessful) {
                    val bitcoin = response.body()
                    bitcoin?.let {
                        insertBitcoin(bitcoin)
                    }
                    return bitcoin
                } else {
                    // Handle API call error if needed
//                    Log.e("TAGY", "API call failed with code: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            // Handle other exceptions if needed
//            Log.e("TAGY", "Exception: ${e.message}")
        }
        return null
    }

    override suspend fun getBitcoinFromNetwork(): Bitcoin? {
        try {
            val response = bitcoinService.getBitcoin()
            if (response.isSuccessful) {
                val bitcoin = response.body()
                if (bitcoin != null) {
                    bitcoinDao.insertBitcoin(bitcoin)
                    bitcoin.values.forEach { dataPrice ->
                        dataPriceDao.insertDataPrice(dataPrice)
                    }
                }
                return bitcoin
            } else {
                // Handle API call error if needed
//                Log.e("TAGY", "API call failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            // Handle other exceptions if needed
//            Log.e("TAGY", "Exception: ${e.message}")
        }
        return null
    }

//    private suspend fun insertBitcoinWithPrices(bitcoin: Bitcoin) {
//        bitcoinDao.insertBitcoin(bitcoin)
//        bitcoin.values.forEach { dataPrices ->
//            dataPriceDao.insertDataPrice(dataPrices)
//        }
//    }

    override suspend fun getBitcoinInfo(): BitcoinInfo? {
        try {
            val localBitcoinInfo = bitcoinInfoDao.getLastBitcoinInfoByPrice()

            if (localBitcoinInfo != null) {
                // Data available locally, return it
                return localBitcoinInfo
            } else {
                // Data not available locally, fetch from API
                val response = bitcoinInfoService.getBitcoinInfo()
                if (response.isSuccessful) {
                    val bitcoinInfo = response.body()
                    if (bitcoinInfo != null) {
                        insertBitcoinInfo(bitcoinInfo)
                    }
                    return bitcoinInfo
                } else {
                    // Handle API call error if needed
//                    Log.e("TAGY", "API call failed with code: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            // Handle other exceptions if needed
//            Log.e("TAGY", "Exception: ${e.message}")
        }
        return null
    }

    override suspend fun getBitcoinInfoFromNetwork(): BitcoinInfo? {
        try {
            val response = bitcoinInfoService.getBitcoinInfo()
            if (response.isSuccessful) {
                val bitcoinInfo = response.body()
                if (bitcoinInfo != null) {
                    bitcoinInfoDao.insertBitcoinInfo(bitcoinInfo)
                }
                return bitcoinInfo
            } else {
                // Handle API call error if needed
                Log.e("TAGY", "API call failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            // Handle other exceptions if needed
            Log.e("TAGY", "Exception: ${e.message}")
        }
        return null
    }

    override suspend fun insertBitcoin(bitcoin: Bitcoin) {
        bitcoinDao.insertBitcoin(bitcoin)
        bitcoin.values.forEach { dataPrices ->
            dataPriceDao.insertDataPrice(dataPrices)
        }
    }

    override suspend fun insertBitcoinInfo(bitcoinInfo: BitcoinInfo) {
        bitcoinInfoDao.insertBitcoinInfo(bitcoinInfo)
    }
}
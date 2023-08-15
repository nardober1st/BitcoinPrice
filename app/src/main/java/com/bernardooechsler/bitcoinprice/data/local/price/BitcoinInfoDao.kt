package com.bernardooechsler.bitcoinprice.data.local.price

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo

@Dao
interface BitcoinInfoDao {

    @Upsert
    suspend fun insertBitcoinInfo(bitcoinInfo: BitcoinInfo)

    @Query("SELECT * FROM bitcoin_info_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastBitcoinInfoByPrice(): BitcoinInfo?
}
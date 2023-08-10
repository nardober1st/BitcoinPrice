package com.bernardooechsler.bitcoinprice.data.local.price

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo

@Dao
interface BitcoinInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBitcoinInfo(bitcoinInfo: BitcoinInfo)

    @Query("SELECT * FROM bitcoin_info_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastBitcoinInfoByPrice(): BitcoinInfo?
}
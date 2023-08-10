package com.bernardooechsler.bitcoinprice.data.local.graph

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin

@Dao
interface BitcoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBitcoin(bitcoin: Bitcoin)

    @Query("SELECT * FROM bitcoin_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastBitcoin(): Bitcoin?
}
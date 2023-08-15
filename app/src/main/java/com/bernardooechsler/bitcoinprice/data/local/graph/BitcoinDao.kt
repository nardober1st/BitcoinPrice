package com.bernardooechsler.bitcoinprice.data.local.graph

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin

@Dao
interface BitcoinDao {

    @Upsert
    suspend fun insertBitcoin(bitcoin: Bitcoin)

    @Query("SELECT * FROM bitcoin_table")
    suspend fun getLastBitcoin(): Bitcoin?
}
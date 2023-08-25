package com.bernardooechsler.bitcoinprice.data.local.graph

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice

@Dao
interface DataPriceDao {

    @Upsert
    suspend fun insertDataPrice(dataPrice: List<DataPrice>)

//    @Query("SELECT * FROM data_price_table ORDER BY id DESC LIMIT 29")
//    fun getLast29DataPricesLiveData(): LiveData<List<DataPrice>>

    @Query("SELECT * FROM data_price_table ORDER BY id DESC LIMIT 2 OFFSET 1")
    suspend fun getYesterdayDataPrice(): DataPrice?

    @Query("SELECT * FROM data_price_table ORDER BY id DESC LIMIT 1")
    suspend fun getTodayDataPrice(): DataPrice?
}
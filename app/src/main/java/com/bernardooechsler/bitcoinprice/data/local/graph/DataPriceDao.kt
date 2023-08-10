package com.bernardooechsler.bitcoinprice.data.local.graph

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice

@Dao
interface DataPriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataPrice(dataPrice: DataPrice)

//    @Query("SELECT * FROM data_price_table ORDER BY id DESC LIMIT 29")
//    fun getLast29DataPricesLiveData(): LiveData<List<DataPrice>>
}
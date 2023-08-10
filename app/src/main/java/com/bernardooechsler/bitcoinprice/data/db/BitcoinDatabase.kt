package com.bernardooechsler.bitcoinprice.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bernardooechsler.bitcoinprice.data.local.graph.BitcoinDao
import com.bernardooechsler.bitcoinprice.data.local.price.BitcoinInfoDao
import com.bernardooechsler.bitcoinprice.data.local.graph.DataPriceDao
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice

@Database(entities = [Bitcoin::class, DataPrice::class, BitcoinInfo::class], version = 2)
@TypeConverters(DataPriceTypeConverter::class)
abstract class BitcoinDatabase : RoomDatabase() {

    abstract fun bitcoinDao(): BitcoinDao
    abstract fun dataPriceDao(): DataPriceDao
    abstract fun bitcoinInfoDao(): BitcoinInfoDao

    companion object {
        private const val DATABASE_NAME = "bitcoin_database"

        @Volatile
        private var INSTANCE: BitcoinDatabase? = null

        fun getDatabase(context: Context): BitcoinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BitcoinDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
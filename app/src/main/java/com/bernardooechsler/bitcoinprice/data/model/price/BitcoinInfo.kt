package com.bernardooechsler.bitcoinprice.data.model.price

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitcoin_info_table")
data class BitcoinInfo(
    @PrimaryKey
    val id: Long = 0,
    val last_trade_price: Double,
    val price_24h: Double,
    val symbol: String,
    val volume_24h: Double
)
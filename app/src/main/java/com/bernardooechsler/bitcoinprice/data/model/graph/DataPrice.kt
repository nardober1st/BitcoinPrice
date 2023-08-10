package com.bernardooechsler.bitcoinprice.data.model.graph

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_price_table")
data class DataPrice(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val x: Int,
    val y: Double
)
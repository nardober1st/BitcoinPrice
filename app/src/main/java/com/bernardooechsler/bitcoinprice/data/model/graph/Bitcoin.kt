package com.bernardooechsler.bitcoinprice.data.model.graph

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitcoin_table")
data class Bitcoin(
    @PrimaryKey
    val id: Long = 0,
    val description: String,
    val name: String,
    val period: String,
    val status: String,
    val unit: String,
    val values: List<DataPrice>
)
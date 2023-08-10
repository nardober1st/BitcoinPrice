package com.bernardooechsler.bitcoinprice.data.db

import androidx.room.TypeConverter
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataPriceTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDataPriceList(dataPriceList: List<DataPrice>): String {
        return gson.toJson(dataPriceList)
    }

    @TypeConverter
    fun toDataPriceList(dataPriceListString: String): List<DataPrice> {
        val type = object : TypeToken<List<DataPrice>>() {}.type
        return gson.fromJson(dataPriceListString, type)
    }
}
package com.bernardooechsler.bitcoinprice.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bernardooechsler.bitcoinprice.data.db.BitcoinDatabase
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo
import com.bernardooechsler.bitcoinprice.data.remote.graph.BitcoinModule
import com.bernardooechsler.bitcoinprice.data.remote.price.BitcoinInfoModule
import com.bernardooechsler.bitcoinprice.data.repository.BitcoinRepositoryImpl
import com.bernardooechsler.bitcoinprice.domain.repository.BitcoinRepository
import kotlinx.coroutines.launch

class BitcoinViewModel(
    private val repository: BitcoinRepository
) : ViewModel() {

    // LiveData to hold the entire Bitcoin data
    private val bitcoinData = MutableLiveData<Bitcoin?>()
    val bitcoinLiveData: LiveData<Bitcoin?> get() = bitcoinData

    // LiveData to hold the DataPrice
    private val _bitcoinDataPrices = MutableLiveData<List<DataPrice>>()
    val bitcoinDataPrices: LiveData<List<DataPrice>> get() = _bitcoinDataPrices

    // New LiveData for BitcoinInfo data
    private val _bitcoinInfo = MutableLiveData<BitcoinInfo?>()
    val bitcoinInfoLiveData: LiveData<BitcoinInfo?> get() = _bitcoinInfo

    fun getBitcoin() {
        viewModelScope.launch {
            try {
                val bitcoin = repository.getBitcoinData()
                bitcoinData.value = bitcoin
                _bitcoinDataPrices.value = bitcoin?.values ?: emptyList()
            } catch (e: Exception) {
                Log.e("TAGY", "Exception: ${e.message}")
            }
        }
    }

    fun getBitcoinInfo() {
        viewModelScope.launch {
            try {
                val bitcoinInfo = repository.getBitcoinInfo()
                _bitcoinInfo.value = bitcoinInfo
            } catch (e: Exception) {
                Log.e("TAGY", "Exception: ${e.message}")
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                // Refresh Bitcoin data
                val bitcoin = repository.getBitcoinFromNetwork()
                bitcoinData.value = bitcoin
                _bitcoinDataPrices.value = bitcoin?.values ?: emptyList()

                // Refresh BitcoinInfo data
                val bitcoinInfo = repository.getBitcoinInfoFromNetwork()
                _bitcoinInfo.value = bitcoinInfo
            } catch (e: Exception) {
                Log.e("TAGY", "Exception: ${e.message}")
            }
        }
    }

    fun observeBitcoinLiveData(owner: LifecycleOwner, observer: Observer<Bitcoin?>) {
        bitcoinLiveData.observe(owner, observer)
    }

    // Function to observe bitcoinDataPrices
    fun observeBitcoinDataPrices(owner: LifecycleOwner, observer: Observer<List<DataPrice>>) {
        bitcoinDataPrices.observe(owner, observer)
    }

    // Function to observe bitcoinInfoLiveData
    fun observeBitcoinInfo(owner: LifecycleOwner, observer: Observer<BitcoinInfo?>) {
        bitcoinInfoLiveData.observe(owner, observer)
    }

    companion object {

        fun create(application: Context): BitcoinViewModel {
            val bitcoinService = BitcoinModule.createBitcoin()
            val bitcoinInfo = BitcoinInfoModule.createBitcoin()
            val bitcoinDatabase = BitcoinDatabase.getDatabase(application)

            val repository = BitcoinRepositoryImpl(
                bitcoinService,
                bitcoinInfo,
                bitcoinDatabase.bitcoinDao(),
                bitcoinDatabase.dataPriceDao(),
                bitcoinDatabase.bitcoinInfoDao(),
            )
            return BitcoinViewModel(repository)
        }
    }
}


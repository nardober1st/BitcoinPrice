package com.bernardooechsler.bitcoinprice

import com.bernardooechsler.bitcoinprice.data.local.graph.BitcoinDao
import com.bernardooechsler.bitcoinprice.data.local.graph.DataPriceDao
import com.bernardooechsler.bitcoinprice.data.local.price.BitcoinInfoDao
import com.bernardooechsler.bitcoinprice.data.model.graph.Bitcoin
import com.bernardooechsler.bitcoinprice.data.model.graph.DataPrice
import com.bernardooechsler.bitcoinprice.data.model.price.BitcoinInfo
import com.bernardooechsler.bitcoinprice.data.remote.graph.BitcoinService
import com.bernardooechsler.bitcoinprice.data.remote.price.BitcoinInfoService
import com.bernardooechsler.bitcoinprice.data.repository.BitcoinRepositoryImpl
import com.bernardooechsler.bitcoinprice.domain.repository.BitcoinRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
class BitcoinRepositoryTest {

    private lateinit var repository: BitcoinRepository
    private val mockBitcoinService: BitcoinService = mock()
    private val mockBitcoinInfoService: BitcoinInfoService = mock()
    val mockBitcoinDao: BitcoinDao = mock()
    private val mockDataPriceDao: DataPriceDao = mock()
    private val mockBitcoinInfoDao: BitcoinInfoDao = mock()

    // Initialize mock DataPrice list
    private val mockDataPrices = listOf(
        DataPrice(0, 1691625600, 29565.82),
        DataPrice(1, 1691539200, 29768.29)
    )

    @Before
    fun setUp() {
        repository = BitcoinRepositoryImpl(
            mockBitcoinService,
            mockBitcoinInfoService,
            mockBitcoinDao,
            mockDataPriceDao,
            mockBitcoinInfoDao
        )
    }

    @Test
    fun `test getBitcoinData success`() = runBlockingTest {
        // Initialize mock Bitcoin instance
        val mockBitcoin = Bitcoin(
            description = "Mock Bitcoin Description",
            name = "Mock Bitcoin Name",
            period = "Mock Bitcoin Period",
            status = "Mock Bitcoin Status",
            unit = "Mock Bitcoin Unit",
            values = mockDataPrices
        )
        whenever(mockBitcoinService.getBitcoin()).thenReturn(Response.success(mockBitcoin))

        // Mock DataPrice insertion
        // ...

        // Call the function to be tested
        val result = repository.getBitcoinData()

        // Verify the expected result
        assertEquals(mockBitcoin, result)
    }

//    @Test
//    fun `test getBitcoinData API error`() = runBlockingTest {
//        // Mock API error response from service
//        whenever(mockBitcoinService.getBitcoin()).thenReturn(Response.error(404, mock()))
//
//        // Call the function to be tested
//        val result = repository.getBitcoinData()
//
//        // Verify that the result is null
//        assertEquals(null, result)
//    }

    @Test
    fun `test getBitcoinData API error`() = runBlockingTest {
        // Mock API error response from service
        `when`(mockBitcoinService.getBitcoin()).thenReturn(null)

        // Call the function to be tested
        val result = repository.getBitcoinData()

        // Verify that the result is null
        assertEquals(null, result)
    }

    @Test
    fun `test getBitcoinInfo success`() = runBlockingTest {
        val mockBitcoinInfo = BitcoinInfo(
            last_trade_price = 40000.0,
            price_24h = 42000.0,
            symbol = "BTC",
            volume_24h = 200000.0
        )
        whenever(mockBitcoinInfoService.getBitcoinInfo()).thenReturn(
            Response.success(
                mockBitcoinInfo
            )
        )

        // Call the function to be tested
        val result = repository.getBitcoinInfo()

        // Verify the expected result
        assertEquals(mockBitcoinInfo, result)
    }

    @Test
    fun `test getBitcoinInfo API error`() = runBlocking {
        BitcoinInfo(1, 100.0, 90.0, "BTC", 1000.0)

        // Mock the behavior of getBitcoinInfo
        `when`(repository.getBitcoinInfo()).thenReturn(null) // Simulate API error

        // Perform the function call
        val result = repository.getBitcoinInfo()

        // Verify the behavior
        assertNull(result) // Verify that the result is null due to API error
    }

    @Test
    fun testDatabaseInteraction() = runBlocking {
        // Mock data
        val mockBitcoin = Bitcoin(
            description = "Mock Bitcoin Description",
            name = "Mock Bitcoin Name",
            period = "Mock Bitcoin Period",
            status = "Mock Bitcoin Status",
            unit = "Mock Bitcoin Unit",
            values = emptyList()
        )
        val mockBitcoinInfo = BitcoinInfo(
            id = 1,
            last_trade_price = 100.0,
            price_24h = 90.0,
            symbol = "BTC",
            volume_24h = 5000.0
        )

        // Mock DAO interactions
        `when`(mockBitcoinDao.getLastBitcoin()).thenReturn(mockBitcoin)
        `when`(mockBitcoinInfoDao.getLastBitcoinInfoByPrice()).thenReturn(mockBitcoinInfo)

        // Call the function to be tested
        val resultBitcoin = repository.getBitcoinData()
        val resultBitcoinInfo = repository.getBitcoinInfo()

        // Verify that the results match the mock data
        assertEquals(mockBitcoin, resultBitcoin)
        assertEquals(mockBitcoinInfo, resultBitcoinInfo)
    }

    @Test
    fun testDataConsistencyOfflineMode() = runBlocking {
        // Mock Bitcoin data
        val mockBitcoin = Bitcoin(
            description = "Mock Bitcoin Description",
            name = "Mock Bitcoin Name",
            period = "Mock Bitcoin Period",
            status = "Mock Bitcoin Status",
            unit = "Mock Bitcoin Unit",
            values = listOf(DataPrice(0, 1691625600, 29565.82), DataPrice(1, 1691539200, 29768.29))
        )

        // Set up repository to return mock Bitcoin data

        `when`(mockBitcoinDao.getLastBitcoin()).thenReturn(mockBitcoin)

        val repository = BitcoinRepositoryImpl(
            mockBitcoinService,
            mockBitcoinInfoService,
            mockBitcoinDao,
            mockDataPriceDao,
            mockBitcoinInfoDao
        )

        // Mock the behavior of your dependencies
        whenever(mockBitcoinDao.getLastBitcoin()).thenReturn(mockBitcoin)

        val result = repository.getBitcoinData()
        assertNotNull(result)
    }
}
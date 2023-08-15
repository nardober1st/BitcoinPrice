package com.bernardooechsler.bitcoinprice.presentation.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bernardooechsler.bitcoinprice.R
import com.bernardooechsler.bitcoinprice.databinding.ActivityHomeBinding
import com.bernardooechsler.bitcoinprice.presentation.BitcoinViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class Home : AppCompatActivity() {

    // Lazy initialization of the ViewModel
    private val viewModel: BitcoinViewModel by lazy {
        BitcoinViewModel.create(applicationContext)
    }
    private lateinit var binding: ActivityHomeBinding

    private lateinit var home: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        home = intent.getStringExtra(MainActivity.USER_NAME).toString()
        binding.tvName.text = "Ola, $home!"

        binding.apply {
            tvBack.visibility = View.INVISIBLE
            tvCliqueAqui.visibility = View.INVISIBLE
            textView4.visibility = View.INVISIBLE
            simpleLine.visibility = View.INVISIBLE
            tvName.visibility = View.INVISIBLE
            lineChart.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            btcDesc.visibility = View.INVISIBLE
            btcName.visibility = View.INVISIBLE
            btcSymbol.visibility = View.INVISIBLE
            btcPrice.visibility = View.INVISIBLE
            aboutChart.visibility = View.INVISIBLE
        }

        // Fetch entire Bitcoin data
        viewModel.getBitcoin()
        // Fetch BitcoinInfo data
        viewModel.getBitcoinInfo()

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            if (isNetworkAvailable()) {
                viewModel.refreshData()
            } else {
                showNoInternetSnackbar()
            }
            // Hide the refresh indicator after the operation is complete
            swipeRefreshLayout.isRefreshing = false
        }

        // Observe bitcoinDataPrices to get the data for the graph
        viewModel.observeBitcoinDataPrices(this) { dataPrices ->
            val prices = ArrayList<Entry>()

            for ((index, dataPrice) in dataPrices.withIndex()) {
                prices.add(Entry(index.toFloat(), dataPrice.y.toFloat()))
            }

            displayLineChart(prices)

            binding.progressBar.postDelayed({
                binding.apply {
                    tvBack.visibility = View.VISIBLE
                    tvCliqueAqui.visibility = View.VISIBLE
                    textView4.visibility = View.VISIBLE
                    simpleLine.visibility = View.VISIBLE
                    tvName.visibility = View.VISIBLE
                    lineChart.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    btcDesc.visibility = View.VISIBLE
                    btcName.visibility = View.VISIBLE
                    btcSymbol.visibility = View.VISIBLE
                    btcPrice.visibility = View.VISIBLE
                    aboutChart.visibility = View.VISIBLE
                }
            }, 500)
        }

        viewModel.observeBitcoinLiveData(this) { bitcoin ->
            binding.btcDesc.text = bitcoin?.description ?: "Bitcoin Name Not Available"
//            binding.btcPrice.text = bitcoin?.values?.lastOrNull()?.y.toString() -> this way we get the last bitcoin price from our dataPrice table (today's price)

            viewModel.viewModelScope.launch {
                val todayDataPrice = viewModel.getTodayDataPrice()
                val yesterdayDataPrice = viewModel.getYesterdayDataPrice()

                if (bitcoin != null && todayDataPrice != null && yesterdayDataPrice != null) {
                    val currentPrice = bitcoin.values.lastOrNull()?.y ?: 0.0
                    val yesterdayPrice = yesterdayDataPrice.y

                    Log.d("TAGY", "Current Price: $currentPrice")
                    Log.d("TAGY", "Yesterday Price: $yesterdayPrice")

                    updatePriceChangePercentage(currentPrice, yesterdayPrice)
                }
            }

        }

// Observe bitcoinInfoLiveData to get the BitcoinInfo data
        viewModel.observeBitcoinInfo(this) { bitcoinInfo -> // this way we fetch bitcoin price from different endpoint and is refresh a few times during the day
            binding.apply {
                btcSymbol.text = "(${bitcoinInfo?.symbol})"
                btcPrice.text = "$${bitcoinInfo?.last_trade_price}"
                btcPrice.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Fetch Bitcoin data again when the activity resumes
        viewModel.getBitcoin()
        // Fetch BitcoinInfo data again when the activity resumes
        viewModel.getBitcoinInfo()
    }

    private fun displayLineChart(prices: ArrayList<Entry>) {

        // Create a LineDataSet with the price entries and set properties
        val lineDataSet = LineDataSet(prices, "Bitcoin Price")
        lineDataSet.color = ContextCompat.getColor(this, R.color.black) // Set the color of the line
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 2f // Set the line width
        lineDataSet.valueTextColor = Color.BLACK // Set the color of value text
        lineDataSet.valueTextSize = 10f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Set the line mode to Cubic Bezier

        lineDataSet.setDrawFilled(true)

        // fill drawable only supported on api level 18 and above
        val drawable = ContextCompat.getDrawable(this, R.drawable.gradient_fill_light)
        lineDataSet.fillDrawable = drawable

        // Create a LineData object with the LineDataSet
        val lineData = LineData(lineDataSet)

        // Assign the LineData to the LineChart
        binding.apply {
            lineChart.data = lineData
            lineChart.description.text = "Bitcoin ChartGraph" // Set the description text
            lineChart.description.textColor = Color.BLACK // Set the description text color
            lineChart.description.textSize = 10f // Set the description text size

            // Customize the XAxis (bottom axis)
            val xAxis = lineChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM // Set the position of the XAxis
            xAxis.setDrawGridLines(false) // Hide vertical grid lines
            xAxis.textColor = Color.BLACK // Set axis label text color
            xAxis.textSize = 12f // Set axis label text size

            // Animate the X-axis (horizontal) entry points with a duration of 1000 milliseconds
            lineChart.animateX(1000)

            // Format the XAxis value using a custom ValueFormatter
            xAxis.valueFormatter = object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < (viewModel.bitcoinDataPrices.value?.size
                            ?: 0)
                    ) {
                        val timestamp = viewModel.bitcoinDataPrices.value?.get(index)?.x ?: 0
                        formatDate(timestamp)
                    } else {
                        ""
                    }
                }
            }

            // Customize the YAxis (left axis)
            val yAxisLeft = lineChart.axisLeft
            yAxisLeft.setDrawGridLines(true) // Hide horizontal grid lines
            yAxisLeft.textColor = Color.BLACK // Set axis label text color
            yAxisLeft.textSize = 12f // Set axis label text size

            // Customize the YAxis (right axis)
            lineChart.axisRight.isEnabled = false // Disable the right axis

            // Customize the chart legend
            val legend = lineChart.legend
            legend.textColor = Color.BLACK // Set legend text color
            legend.textSize = 14f // Set legend text size
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT // Set legend position

            // Add this after setting up your lineDataSet
            lineDataSet.setDrawIcons(false) // Hide data set icons
            lineDataSet.setDrawValues(false) // Hide data set values

            // Enable and customize the interactive legend
            legend.form = Legend.LegendForm.LINE // Use line instead of default square
            legend.formSize = 12f
            legend.textSize = 12f
            legend.isWordWrapEnabled = true
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.yEntrySpace = 10f
            legend.xEntrySpace = 10f

            // Enable hiding/showing data sets on legend click
            lineChart.isHighlightPerTapEnabled = true
            lineChart.isHighlightPerDragEnabled = false // Disable highlighting by dragging

            lineChart.invalidate() // Refresh the chart
        }

        binding.tvCliqueAqui.setOnClickListener {

            startConversionActivity()
        }

        binding.tvBack.setOnClickListener {

            startMainActivityActivity()
        }
    }

    private fun formatDate(timestamp: Int): String {
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000L)
        return sdf.format(date)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isAvailable = networkInfo?.isConnectedOrConnecting ?: false

        if (isAvailable) {
            // Show the "Connected to the internet!" Snackbar
            showRefreshedSnackbar()
        } else {
            // Show the "No internet connection" Snackbar
            showNoInternetSnackbar()
        }
        return isAvailable
    }

    private fun showRefreshedSnackbar() {
        val snackbar = Snackbar.make(
            binding.btcName, "Data refreshed.", Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }

    private fun showNoInternetSnackbar() {
        val snackbar =
            Snackbar.make(
                binding.btcName,
                "No internet connection. Try again later",
                Snackbar.LENGTH_SHORT
            )
        snackbar.show()
    }

    private fun startConversionActivity() {

        val intent = Intent(this, ConversationScreen::class.java)
        intent.putExtra(MainActivity.USER_NAME, home)
        startActivity(intent)
    }

    private fun startMainActivityActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updatePriceChangePercentage(currentPrice: Double, yesterdayPrice: Double) {
        val priceChangePercentage = ((currentPrice - yesterdayPrice) / yesterdayPrice) * 100
        val priceChangeFormatted = String.format("%.2f%%", priceChangePercentage)

//        val priceChangeTextView = findViewById<TextView>(R.id.tvPriceChange)
//        val arrowImageView = findViewById<ImageView>(R.id.ivArrow)

        binding.tvPercentage.text = priceChangeFormatted

        if (priceChangePercentage > 0) {
            binding.ivArrow.setImageResource(R.drawable.ic_up_arrow)
        } else if (priceChangePercentage < 0) {
            binding.ivArrow.setImageResource(R.drawable.ic_down_arrow)
        } else {
            binding.ivArrow.setImageDrawable(null)
        }
    }
}
package com.bernardooechsler.bitcoinprice.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bernardooechsler.bitcoinprice.R
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ConversationScreen : AppCompatActivity() {

    // Constants:
    // TODO: Create the base URL
    private val BASE_URL = "http://api.coinlayer.com/api/live?access_key="

    // Member Variables:
    var mPriceTextView: TextView? = null

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_screen)

        name = intent.getStringExtra(MainActivity.USER_NAME)

        val voltar = findViewById<TextView>(R.id.tv_voltar)
        voltar.setOnClickListener {
            startHomeActivity()
        }

        mPriceTextView = findViewById<View>(R.id.priceLabel) as TextView
        val spinner = findViewById<View>(R.id.currency_spinner) as Spinner


        // Create an ArrayAdapter using the String array and a spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.currency_array, R.layout.spinner_item
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val publicKey = "200a70d06258bc5ae79eb01cbd167c33"
                val finalUrl =
                    BASE_URL + publicKey + "&TARGET=" + adapterView.getItemAtPosition(i) + "&symbols=BTC"
                Log.d("Clima", "Request fail! Status code: $finalUrl")
                try {
                    letsDoSomeNetworking(finalUrl)
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    // TODO: complete the letsDoSomeNetworking() method
    @Throws(IOException::class, JSONException::class)
    private fun letsDoSomeNetworking(url: String) {
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {
                // called when response HTTP status is "200 OK"
                Log.d("Clima", "JSON: $response")
                try {
                    val price = response.getJSONObject("rates")
                    val `object` = price.getString("BTC")
                    mPriceTextView!!.text = `object`
                } catch (E: JSONException) {
                    E.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int, headers: Array<Header>, e: Throwable, response: JSONObject
            ) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Clima", "Request fail! Status code: $statusCode")
                Log.d("Clima", "Fail response: $response")
                Log.e("ERROR", e.toString())
            }
        }]
    }

    private fun startHomeActivity() {

        val intent = Intent(this,Home::class.java)
        intent.putExtra(MainActivity.USER_NAME, name)
        startActivity(intent)
    }
}










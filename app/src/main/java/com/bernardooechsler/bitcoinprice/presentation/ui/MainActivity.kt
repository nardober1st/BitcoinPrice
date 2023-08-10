package com.bernardooechsler.bitcoinprice.presentation.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bernardooechsler.bitcoinprice.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        const val USER_NAME: String = "user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val btnDone = findViewById<Button>(R.id.buttom_login)

        btnDone.setOnClickListener {

            val edtname = findViewById<EditText>(R.id.edt_nome)
            val edtpassword = findViewById<EditText>(R.id.edt_password)

            val name = edtname.text.toString()
            val password = edtpassword.text.toString()

            startHomeActivity(name)

            when {

                name.isEmpty() -> {
                    mensage(it, "Coloque seu nome aqui!")

                }

                password.isEmpty() -> {
                    mensage(it, "Preencha a senha!")

                }

                password.length <= 5 -> {
                    mensage(it, "A senha precisa ter no mínimo 6 caracteres!")

                }

                else -> {

                    //  startHomeActivity(name)
                }
            }
        }
    }

    private fun mensage(view: View, mensage: String) {

        val snackbar = Snackbar.make(view, mensage, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF6347"))
        snackbar.setTextColor(Color.parseColor("#FFFFFFFF"))
        snackbar.show()
    }


    private fun startHomeActivity(name: String) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra(USER_NAME, name)
        startActivity(intent)
    }
}
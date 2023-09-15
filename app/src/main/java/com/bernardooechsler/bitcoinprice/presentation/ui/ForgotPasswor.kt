package com.bernardooechsler.bitcoinprice.presentation.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bernardooechsler.bitcoinprice.R
import com.bernardooechsler.bitcoinprice.databinding.ActivityForgotPassworBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswor : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassworBinding
    private val Auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassworBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnreset.setOnClickListener {



            val emailPass = binding.edtemailLogin.text.toString()

            Auth.sendPasswordResetEmail(emailPass)
                .addOnCompleteListener { void->

                    val color = Color.parseColor("#FF03DAC5")
                    val snackbar =
                        Snackbar.make(it, "Verifique seu E-mail", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(color)
                    snackbar.show()




                }.addOnFailureListener {void->



                    val snackbar =
                        Snackbar.make(it, "Server Error.", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()



                }








        }











    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}
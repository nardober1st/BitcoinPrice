package com.bernardooechsler.bitcoinprice.presentation.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bernardooechsler.bitcoinprice.R
import com.bernardooechsler.bitcoinprice.databinding.ActivitySingUpScreenBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SingUpScreen : AppCompatActivity() {


    private lateinit var binding: ActivitySingUpScreenBinding
    private val Auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.buttomRegistration.setOnClickListener { view ->


            val email = binding.edtEmailRegistration.text.toString()
            val password = binding.edtPasswordRegistration.toString()


            if (email.isEmpty() || password.isEmpty()) {

                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            } else {

                Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                    if (it.isSuccessful) {

                        val snackbar =
                            Snackbar.make(view, "Usuário Cadastrado", Snackbar.LENGTH_SHORT)
                        val backgroundColor = Color.parseColor("#FF03DAC5")
                        snackbar.setBackgroundTint(backgroundColor)
                        snackbar.show()

                        binding.edtEmailRegistration.setText("")
                        binding.edtPasswordRegistration.setText("")


                    }


                }.addOnFailureListener{exception->



                    val ErrorMensage = when(exception){


                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres."
                        is FirebaseAuthInvalidCredentialsException -> "Digite um email válido."
                        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado."
                        is FirebaseNetworkException -> "Sem conexão com a internet."
                        else -> "Erro ao cadastrar usuário."


                    }


                    val snackbar =
                        Snackbar.make(view, ErrorMensage, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()



                }


            }


        }


    }


}
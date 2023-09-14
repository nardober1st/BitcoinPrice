package com.bernardooechsler.bitcoinprice.presentation.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bernardooechsler.bitcoinprice.R
import com.bernardooechsler.bitcoinprice.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    companion object {
        const val USER_NAME: String = "user_name"
    }

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val btnDone = findViewById<Button>(R.id.buttom_login)
        val btnRegistration = findViewById<Button>(R.id.buttom_singUp)
        val TvEsqueci = findViewById<TextView>(R.id.tv_esqueci)

        btnRegistration.setOnClickListener {

            startRegistrationActivity()


        }
        TvEsqueci.setOnClickListener {

            startForgotActivity()


        }





        btnDone.setOnClickListener {view->

            val edtname = findViewById<EditText>(R.id.edt_nome)
            val edtpassword = findViewById<EditText>(R.id.edt_password)

            val email = edtname.text.toString()
            val password = edtpassword.text.toString()



            if (email.isEmpty() || password.isEmpty()){




              mensageFail(view, "Preencha todos os Campos." )





            }else{


            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {Autentic ->

                if(Autentic.isSuccessful){


                    startHomeActivity("")


                }



            }.addOnFailureListener { exeption ->

                val ErrorMensage = when (exeption) {


                    is FirebaseAuthInvalidCredentialsException -> "Senha invalida."
                    is FirebaseAuthInvalidUserException -> "E-mail invalido."
                    is FirebaseNetworkException -> "Sem conexão com a internet."
                    else -> "Server Error."

                }


                val snackbar =
                    Snackbar.make(view, ErrorMensage, Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }








            }

          /*  when {

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

                    startHomeActivity(name)
                }
            }*/
        }
    }

    private fun mensage(view: View, mensage: String) {

        val snackbar = Snackbar.make(view, mensage, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF6347"))
        snackbar.setTextColor(Color.parseColor("#FFFFFFFF"))
        snackbar.show()
    }
    private fun mensageFail(view: View, mensage: String) {

        val snackbar = Snackbar.make(view, mensage, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.RED)
        snackbar.setTextColor(Color.parseColor("#FFFFFFFF"))
        snackbar.show()
    }


    private fun startHomeActivity(name: String) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra(USER_NAME, name)
        startActivity(intent)
        finish()
    }

    private fun startRegistrationActivity() {
        val intent = Intent(this, SingUpScreen::class.java)
        startActivity(intent)
    }

    private fun startForgotActivity() {
        val intent = Intent(this, ForgotPasswor::class.java)
        startActivity(intent)
    }





}



/*override fun onStart() {
    super.onStart()

    val userAtual = FirebaseAuth.getInstance().currentUser

    if (userAtual != null) {

        startHomeActivity("")

    }

}*/
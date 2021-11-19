package com.example.philomathy


import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.text.Editable

import android.text.TextWatcher


class LoginActivity : AppCompatActivity() {

    private lateinit var loginbutton: Button
    private lateinit var signupbutton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        signupbutton = findViewById(R.id.signupbutton)
        emailInput = findViewById(R.id.textUsernameLayout)
        passwordInput = findViewById(R.id.textPasswordInput)
        loginbutton = findViewById(R.id.loginButton)


        auth = Firebase.auth

        loginbutton.setOnClickListener { onLoginClicked() }

        signupbutton.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        emailInput
            .editText
            ?.addTextChangedListener(createTextWatcher(emailInput))
        passwordInput
            .editText
            ?.addTextChangedListener(createTextWatcher(emailInput))


    }

    private fun onLoginClicked() {
        val username: String = emailInput.editText?.text.toString()
        val password: String = passwordInput.editText?.text.toString()
        when {
            username.isEmpty() -> {
                emailInput.error = "Username must not be empty"
            }
            password.isEmpty() -> {
                passwordInput.error = "Password must not be empty"
            }
            else -> {
                performLogin()
            }
        }


    }


    private fun performLogin() {


        val email = findViewById<TextInputLayout>(R.id.textUsernameLayout).editText?.text.toString()
        val password =
            findViewById<TextInputLayout>(R.id.textPasswordInput).editText?.text.toString()

        login(email, password)

    }

    private fun login(email: String, password: String) {

        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext, "Authentication Success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("getUser", auth.currentUser)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showErrorDialog()
                }
            }

    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Failed")
            .setMessage("Username or password is not correct. Please try again.")
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser


        }


    private fun createTextWatcher(textPasswordInput: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int, count: Int, after: Int
            ) {
                // not needed
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int, before: Int, count: Int
            ) {
                textPasswordInput.error = null
            }

            override fun afterTextChanged(s: Editable) {
                // not needed
            }
        }
    }


}
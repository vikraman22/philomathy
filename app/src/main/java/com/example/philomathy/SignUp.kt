package com.example.philomathy

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth


//getCurrentUser

class SignUp : AppCompatActivity() {

    private lateinit var signupbutton : Button
    private lateinit var usernameInput : TextInputLayout
    private lateinit var emailInput : TextInputLayout
    private lateinit var passwordInput : TextInputLayout
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        signupbutton = findViewById(R.id.loginButton)
        emailInput = findViewById(R.id.textUsernameLayout)
        passwordInput = findViewById(R.id.textPasswordInput)
        usernameInput = findViewById(R.id.textUsername1Layout)

        signupbutton.setOnClickListener {

            onsignUpClicked()
        }

        emailInput
            .editText
            ?.addTextChangedListener(createTextWatcher(emailInput))
        passwordInput
            .editText
            ?.addTextChangedListener(createTextWatcher(emailInput))
        usernameInput
            .editText
            ?.addTextChangedListener(createTextWatcher(emailInput)
            )


    }

    private fun onsignUpClicked() {
        val username: String = usernameInput.editText?.text.toString()
        val password: String = passwordInput.editText?.text.toString()
        val email : String = emailInput.editText?.text.toString()

        when {
            username.isEmpty() -> {
                usernameInput.error = "Username must not be empty"
            }
            password.isEmpty() -> {
                passwordInput.error = "Password must not be empty"
            }
            email.isEmpty() ->
            {
                emailInput.error = "Email must not be empty"
            }

            else -> {
                performSignup()
            }
        }

    }

    private fun performSignup() {
        val email = findViewById<TextInputLayout>(R.id.textUsernameLayout).editText?.text.toString()
        val password =
            findViewById<TextInputLayout>(R.id.textPasswordInput).editText?.text.toString()
        val username = findViewById<TextInputLayout>(R.id.textUsername1Layout).editText?.text.toString()


        signup(email, password)

    }

    private fun signup(email: String, password: String) {

        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    val user = auth.currentUser

                    Toast.makeText(baseContext, "$user?.email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
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
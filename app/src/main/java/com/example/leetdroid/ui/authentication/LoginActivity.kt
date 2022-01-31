package com.example.leetdroid.ui.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.leetdroid.databinding.ActivityLoginBinding
import com.example.leetdroid.extensions.openActivity
import com.example.leetdroid.utils.StringExtensions.isEmailValid
import com.example.leetdroid.utils.hideSoftKeyboard

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // hide the action bar
        val actionBar: ActionBar? = supportActionBar
        supportActionBar?.hide()

        loginBinding.registerButton.setOnClickListener {
            openActivity(SignUpActivity::class.java)
            finish()
        }

        loginBinding.loginCardLayout.setOnClickListener {
            hideSoftKeyboard(this)
        }

        loginBinding.forgotPasswordText.setOnClickListener {
            openActivity(ForgotPasswordActivity::class.java)
        }
        loginBinding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                when {
                    loginBinding.emailEditText.text.toString()
                        .isEmpty() -> loginBinding.emailLayoutLogin.error = "Please enter an email "

                    !loginBinding.emailEditText.text.toString().trim{ it <=' '}.isEmailValid() ->
                        loginBinding.emailLayoutLogin.error = "Please enter a valid email"

                    else ->
                        loginBinding.emailLayoutLogin.error = null
                }
            }
        })

        loginBinding.passwordEditText.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {}

                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    when {
                        loginBinding.passwordEditText.text.toString()
                            .isEmpty() -> loginBinding.passwordLayoutLogin.error =
                            "Please enter a password"

                        loginBinding.passwordEditText.text.toString().length < 6 -> loginBinding.passwordLayoutLogin.error =
                            "Password length should be more than 6 characters"

                        else -> loginBinding.passwordLayoutLogin.error = null
                    }
                }
            })

        loginBinding.loginButton.setOnClickListener {
            hideSoftKeyboard(this)
            if (loginBinding.emailEditText.text.toString().trim{ it <=' '}.isEmpty()) {
                loginBinding.emailLayoutLogin.error = "Please enter an email"
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().isEmpty()) {
                loginBinding.passwordLayoutLogin.error = "Please enter a password"
                return@setOnClickListener
            }

            if (!loginBinding.emailEditText.text.toString().trim{ it <=' '}.isEmailValid()) {
                loginBinding.emailLayoutLogin.error = "Please enter a valid email"
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().length < 6)
                loginBinding.passwordLayoutLogin.error =
                    "Password length should be more than 6 characters"
            return@setOnClickListener
        }

        loginBinding.emailLayoutLogin.error = null
        loginBinding.passwordLayoutLogin.error = null

    }
}
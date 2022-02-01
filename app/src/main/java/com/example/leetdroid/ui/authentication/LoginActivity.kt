package com.example.leetdroid.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.leetdroid.data.entitiy.FirebaseUserProfile
import com.example.leetdroid.data.viewModel.FirebaseUserViewModel
import com.example.leetdroid.databinding.ActivityLoginBinding
import com.example.leetdroid.extensions.openActivity
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.ui.base.MainActivity
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.StringExtensions.isEmailValid
import com.example.leetdroid.utils.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var firebaseUserViewModel: FirebaseUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // hide the action bar
        supportActionBar?.hide()

        // check if user is signed in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            // User is signed out
            Log.d(Constant.TAG("LoginActivity").toString(), "user not signed in!")
        }

        firebaseUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FirebaseUserViewModel::class.java]

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

                    !loginBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmailValid() ->
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
            if (loginBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                loginBinding.emailLayoutLogin.error = "Please enter an email"
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().isEmpty()) {
                loginBinding.passwordLayoutLogin.error = "Please enter a password"
                return@setOnClickListener
            }

            if (!loginBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmailValid()) {
                loginBinding.emailLayoutLogin.error = "Please enter a valid email"
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().length < 6) {
                loginBinding.passwordLayoutLogin.error =
                    "Password length should be more than 6 characters"
                return@setOnClickListener
            }

            loginBinding.emailLayoutLogin.error = null
            loginBinding.passwordLayoutLogin.error = null

            val email: String = loginBinding.emailEditText.text.toString().trim { it <= ' ' }
            val password: String = loginBinding.passwordEditText.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val user = FirebaseAuth.getInstance().currentUser
                        showSnackBar(this, "Login Successful!")

                        // fetch data from firebase and put it in local database
                        fetchUserData()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Log.d(
                            Constant.TAG("LoginActivity").toString(),
                            "cause: ${task.exception?.cause.toString()}"
                        )
                        Log.d(
                            Constant.TAG("LoginActivity").toString(),
                            "message:${task.exception?.message.toString()}"
                        )
                        showSnackBar(this, task.exception?.message.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(Constant.TAG("LoginActivity").toString(), "cause: ${it.cause.toString()}")
                    Log.d(
                        Constant.TAG("LoginActivity").toString(),
                        "message:${it.message.toString()}"
                    )
                    showSnackBar(this, it.message.toString())
                }
        }
    }

    private fun fetchUserData() {
        val user = FirebaseAuth.getInstance().currentUser
        Firebase.firestore.collection("users").document(user!!.uid).get()
            .addOnCompleteListener {
                val doc = it.result
                if (doc != null) {
                    val firebaseUserProfile = FirebaseUserProfile(
                        doc.getString("uuid").toString(),
                        doc.getString("email").toString(),
                        doc.getString("username").toString(),
                    )
                    firebaseUserViewModel.addUser(firebaseUserProfile)
                }
            }.addOnFailureListener {
                Log.d(Constant.TAG("LoginActivity").toString(), it.message.toString())
                showSnackBar(this, it.message)
            }

    }


}
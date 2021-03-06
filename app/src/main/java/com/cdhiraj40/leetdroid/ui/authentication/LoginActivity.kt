package com.cdhiraj40.leetdroid.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cdhiraj40.leetdroid.data.entitiy.FirebaseUserProfile
import com.cdhiraj40.leetdroid.data.viewModel.FirebaseUserViewModel
import com.cdhiraj40.leetdroid.databinding.ActivityLoginBinding
import com.cdhiraj40.leetdroid.utils.extensions.openActivity
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar
import com.cdhiraj40.leetdroid.ui.base.MainActivity
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.SharedPreferences
import com.cdhiraj40.leetdroid.utils.StringExtensions.isEmailValid
import com.cdhiraj40.leetdroid.utils.hideSoftKeyboard
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
            Log.d(Constant.TAG(LoginActivity::class.java).toString(), "user not signed in!")
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
            loginBinding.loginProgressBar.visibility = View.VISIBLE
            hideSoftKeyboard(this)
            if (loginBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                loginBinding.emailLayoutLogin.error = "Please enter an email"
                loginBinding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().isEmpty()) {
                loginBinding.passwordLayoutLogin.error = "Please enter a password"
                loginBinding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (!loginBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmailValid()) {
                loginBinding.emailLayoutLogin.error = "Please enter a valid email"
                loginBinding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (loginBinding.passwordEditText.text.toString().length < 6) {
                loginBinding.passwordLayoutLogin.error =
                    "Password length should be more than 6 characters"
                loginBinding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            loginBinding.emailLayoutLogin.error = null
            loginBinding.passwordLayoutLogin.error = null

            val email: String = loginBinding.emailEditText.text.toString().trim { it <= ' ' }
            val password: String = loginBinding.passwordEditText.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        loginBinding.loginProgressBar.visibility = View.GONE
                        showSnackBar(this, "Login Successful!")
                        // fetch data from firebase and put it in local database
                        fetchUserData()

                    } else {
                        Log.d(
                            Constant.TAG(LoginActivity::class.java).toString(),
                            "cause: ${task.exception?.cause.toString()}"
                        )
                        Log.d(
                            Constant.TAG(LoginActivity::class.java).toString(),
                            "message:${task.exception?.message.toString()}"
                        )
                        loginBinding.loginProgressBar.visibility = View.GONE
                        showSnackBar(this, task.exception?.message.toString())
                    }
                }
                .addOnFailureListener {
                    loginBinding.loginProgressBar.visibility = View.GONE
                    Log.d(
                        Constant.TAG(LoginActivity::class.java).toString(),
                        "cause: ${it.cause.toString()}"
                    )
                    Log.d(
                        Constant.TAG(LoginActivity::class.java).toString(),
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
                    // check if app is newly installed then add user else only update
                    if (!SharedPreferences(this).firebaseUserAdded) {
                        firebaseUserViewModel.addUser(firebaseUserProfile)
                        SharedPreferences(this).firebaseUserAdded = true
                    } else {
                        firebaseUserProfile.id = 1
                        firebaseUserViewModel.updateUser(firebaseUserProfile)
                    }
                    login()
                }
            }.addOnFailureListener {
                Log.d(Constant.TAG(LoginActivity::class.java).toString(), it.message.toString())
                showSnackBar(this, it.message)
            }
    }

    private fun login() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
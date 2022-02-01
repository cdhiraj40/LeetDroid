package com.example.leetdroid.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.leetdroid.api.ApiResponseListener
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.data.entitiy.FirebaseUserProfile
import com.example.leetdroid.data.viewModel.FirebaseUserViewModel
import com.example.leetdroid.databinding.ActivitySignUpBinding
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.model.FirebaseUserModel
import com.example.leetdroid.model.UserProfileErrorModel
import com.example.leetdroid.model.UserProfileModel
import com.example.leetdroid.ui.base.MainActivity
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.StringExtensions.isEmailValid
import com.example.leetdroid.utils.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firebaseUserViewModel: FirebaseUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        // hide the action bar
//        val actionBar: ActionBar? = supportActionBar
        supportActionBar?.hide()

        firebaseUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FirebaseUserViewModel::class.java]

        signUpBinding.loginButton.setOnClickListener {
            onBackPressed()
            finish()
        }

        signUpBinding.signupCardLayout.setOnClickListener {
            hideSoftKeyboard(this)
        }


        signUpBinding.emailEditText.addTextChangedListener(object : TextWatcher {
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
                if (signUpBinding.emailEditText.text.toString().isEmpty())
                    signUpBinding.emailLayoutSignup.error = "Please enter an email"
                else if (!signUpBinding.emailEditText.text.toString().trim { it <= ' ' }
                        .isEmailValid())
                    signUpBinding.emailLayoutSignup.error = "Please enter a valid email"
                else
                    signUpBinding.emailLayoutSignup.error = null
            }
        })

        signUpBinding.passwordEditText.addTextChangedListener(object : TextWatcher {
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
                    signUpBinding.passwordEditText.text.toString()
                        .isEmpty() -> signUpBinding.passwordLayoutSignup.error =
                        "Please enter a password"
                    signUpBinding.passwordEditText.text.toString().length < 6 -> signUpBinding.passwordLayoutSignup.error =
                        "Password length should be more than 6 characters"
                    else -> signUpBinding.passwordLayoutSignup.error = null
                }
            }
        })

        signUpBinding.usernameEditText.addTextChangedListener(object : TextWatcher {
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
                signUpBinding.usernameLayoutSignup.error = null
            }
        })

        signUpBinding.registerButton.setOnClickListener {
            signUpBinding.usernameLayoutSignup.error = null
            hideSoftKeyboard(this)
            if (signUpBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                signUpBinding.emailLayoutSignup.error = "Please enter an email"
                return@setOnClickListener
            }

            if (signUpBinding.passwordEditText.text.toString().isEmpty()) {
                signUpBinding.passwordLayoutSignup.error = "Please enter a password"
                return@setOnClickListener
            }

            if (!signUpBinding.emailEditText.text.toString().trim { it <= ' ' }.isEmailValid()) {
                signUpBinding.emailLayoutSignup.error = "Please enter a valid email"
                return@setOnClickListener
            }

            if (signUpBinding.passwordEditText.text.toString().length < 6) {
                signUpBinding.passwordLayoutSignup.error =
                    "Password length should be more than 6 characters"
                return@setOnClickListener
            }

            val username = signUpBinding.usernameEditText.text.toString().trim { it <= ' ' }
                .lowercase(Locale.getDefault())

            if (username.isEmpty()) {
                signUpBinding.usernameLayoutSignup.error = "Please enter a username"
                return@setOnClickListener
            }
            val email: String = signUpBinding.emailEditText.text.toString().trim { it <= ' ' }
            val password: String = signUpBinding.passwordEditText.text.toString().trim { it <= ' ' }

            // check if user exist
            loadUser(username, object : ApiResponseListener {
                override fun onSuccess(success: Boolean) {
                    if (success) {
                        runOnUiThread {
                            signUpBinding.emailLayoutSignup.error = null
                            signUpBinding.passwordLayoutSignup.error = null

                            registerUser(email, password, username)
                            signUpBinding.signupProgressBar.visibility = View.GONE
                        }
                    } else {
                        runOnUiThread {
                            signUpBinding.usernameLayoutSignup.error = "User does not exist"
                            signUpBinding.signupProgressBar.visibility = View.GONE
                        }
                    }
                }
            })


        }
    }

    private fun registerUser(email: String, password: String, username: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // user registered
                    val firebaseUser = task.result!!.user
                    val uid = firebaseUser!!.uid
                    val firestoreDB = FirebaseFirestore.getInstance()

                    val firebaseUserModel = FirebaseUserModel(uid, email, username)

                    firestoreDB.collection("users").document(uid).set(firebaseUserModel)
                    showSnackBar(this, "You are registered!")

                    addUserInDB(FirebaseUserProfile(uid, email, username))
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    // registration failed
                    Log.d(
                        Constant.TAG("SignUpActivity").toString(),
                        task.exception!!.message.toString()
                    )
                    showSnackBar(this, task.exception!!.message.toString())
                }
            }
    }


    // creates an okHttpClient call for user data
    private fun createApiCall(username: String): Call {
        val okHttpClient = OkHttpClient()
        val postBody =
            Gson().toJson(LeetCodeRequests.Helper.getUserProfileRequest(username))
        val requestBody: RequestBody =
            postBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .build()
        val request: Request = Request.Builder()
            .headers(headers)
            .post(requestBody)
            .url(URL.graphql)
            .build()
        return okHttpClient.newCall(request)
    }

    // load user from online
    private fun loadUser(username: String, apiResponseListener: ApiResponseListener) {
        signUpBinding.signupProgressBar.visibility = View.VISIBLE

        val call: Call = createApiCall(username)
        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(Constant.TAG("SignUpActivity").toString(), call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body!!.string()
                val userData: UserProfileModel = JsonUtils.generateObjectFromJson(
                    body,
                    UserProfileModel::class.java
                )
                if (userData.data?.matchedUser == null) {
                    val errorData: UserProfileErrorModel = JsonUtils.generateObjectFromJson(
                        body,
                        UserProfileErrorModel::class.java
                    )
                    if (errorData.errors?.get(0)?.message.toString() == "That user does not exist.") {
                        apiResponseListener.onSuccess(false)
                    } else {
                        signUpBinding.usernameLayoutSignup.error =
                            "Something went wrong, please try again later"
                        return
                    }
                } else {
                    apiResponseListener.onSuccess(true)
                }
            }
        })
    }

    private fun addUserInDB(firebaseUserProfile: FirebaseUserProfile) {
        lifecycleScope.launch {
            firebaseUserViewModel.addUser(firebaseUserProfile)
        }
    }
}
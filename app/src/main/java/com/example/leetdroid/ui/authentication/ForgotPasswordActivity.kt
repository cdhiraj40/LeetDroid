package com.example.leetdroid.ui.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.leetdroid.R
import com.example.leetdroid.databinding.ActivityForgotPasswordBinding
import com.example.leetdroid.utils.extensions.showSnackBarWithAction
import com.example.leetdroid.utils.CommonUtils
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.StringExtensions.isEmailValid
import com.example.leetdroid.utils.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth


// TODO check email if its registered before sending email
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotPasswordBinding.root)

        // hide the action bar
        val actionBar: ActionBar? = supportActionBar
        supportActionBar?.hide()

        forgotPasswordBinding.emailEditText.addTextChangedListener(object : TextWatcher {
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
                    forgotPasswordBinding.emailEditText.text.toString()
                        .isEmpty() -> forgotPasswordBinding.emailLayoutForgotPassword.error =
                        "Please enter an email "

                    !forgotPasswordBinding.emailEditText.text.toString().trim { it <= ' ' }
                        .isEmailValid() ->
                        forgotPasswordBinding.emailLayoutForgotPassword.error =
                            "Please enter a valid email"

                    else ->
                        forgotPasswordBinding.emailLayoutForgotPassword.error = null
                }
            }
        })

        // not sure why it was not working
//        forgotPasswordBinding.forgotPasswordBackButton.setOnClickListener {
//            showSnackBar(this, "Asdasd")
//            openActivity(LoginActivity::class.java)
//            finish()
//        }

        forgotPasswordBinding.forgotPasswordButton.setOnClickListener {
            hideSoftKeyboard(activity = this)
            forgotPassword(forgotPasswordBinding.emailEditText.text.toString())
        }
    }

    private fun forgotPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Constant.TAG(ForgotPasswordActivity::class.java).toString(), "Email sent.")
                    showSnackBarWithAction(
                        this,
                        "Email has been sent to if user exists!",
                        getString(R.string.prompt_open_email),
                        null
                    ) {
                        CommonUtils.openEmail(context = this, activity = this)
                    }
                    forgotPasswordBinding.forgotPasswordButtonText.text =
                        getString(R.string.sent_email_button_text)
                    forgotPasswordBinding.forgotPasswordButton.isEnabled = false
                    forgotPasswordBinding.forgotPasswordSubTitle.visibility = View.VISIBLE
                    forgotPasswordBinding.emailEditText.isEnabled = false
                }
            }
    }
}
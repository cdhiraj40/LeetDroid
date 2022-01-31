package com.example.leetdroid.ui.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.leetdroid.R
import com.example.leetdroid.databinding.ActivityForgotPasswordBinding
import com.example.leetdroid.extensions.openActivity
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.utils.StringExtensions.isEmailValid

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

                    !forgotPasswordBinding.emailEditText.text.toString().trim{ it <=' '}.isEmailValid() ->
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
            forgotPasswordBinding.emailEditText.isEnabled = false
            forgotPasswordBinding.forgotPasswordButtonText.text =
                getString(R.string.sent_email_button_text)
            forgotPasswordBinding.forgotPasswordSubTitle.visibility = View.VISIBLE
        }
    }
}
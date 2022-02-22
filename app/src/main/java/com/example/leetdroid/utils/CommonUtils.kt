package com.example.leetdroid.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.leetdroid.utils.Constant.Companion.TAG
import com.example.leetdroid.utils.DateUtils.getCurrentTime
import com.example.leetdroid.utils.extensions.showSnackBar
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipFile

object CommonUtils {
    val NEW_LINE_CHAR = System.getProperty("line.separator")

    fun openEmail(context: Context, activity: Activity) {
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showSnackBar(activity, "There is no email client installed")
        }
    }

    // open the link in browser
    fun openLink(context: Context, link: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(link)
            )
        )
    }

    fun composeEmail(context: Context, recipient: String, subject: String, body: String) {

        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        val urlString =
            "mailto:" + Uri.encode(recipient) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(
                body
            )
        selectorIntent.data = Uri.parse(urlString)

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        emailIntent.selector = selectorIntent

        context.startActivity(Intent.createChooser(emailIntent, "Bug Report"))
    }

    /**
     * Gets all the appropriate version names, for the app, the Android version and API
     * Manufacturer and Model name of the phone
     *
     * @return the string containing all the above
     */
    fun getVersionNames(baseContext: Context): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Version Details\n")
        val versionName: String = getVersionString(baseContext)
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val version = Build.VERSION.SDK_INT
        val versionRelease = Build.VERSION.RELEASE
        stringBuilder.append("LeetDroid : ")
        stringBuilder.append(versionName)
        stringBuilder.append("\n Manufacturer: $manufacturer")
        stringBuilder.append("\n Model: $model")
        stringBuilder.append("\n Android API Level: $version")
        stringBuilder.append("\n Version Release: $versionRelease")
        stringBuilder.append("\n")
        return stringBuilder.toString()
    }

    /**
     * method to get the current version
     */
    fun getVersionString(context: Context): String {
        val out = StringBuilder("Version ")
        try {
            out.append(getVersionStringShort(context))
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(TAG(CommonUtils::class.java), e.message, e)
        }
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName, 0
            )
            val zipFile = ZipFile(ai.sourceDir)
            val zipEntry = zipFile.getEntry("classes.dex")
            val time = zipEntry.time
            out.append(SimpleDateFormat.getInstance().format(Date(time)))
            zipFile.close()
        } catch (e: Exception) {
            Log.d(TAG(CommonUtils::class.java), e.message, e)
        }
        return out.toString()
    }

    @Throws(PackageManager.NameNotFoundException::class)
    fun getVersionStringShort(context: Context): String? {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }

    /**
     * read the getUserStatistic defined edit fields.
     *
     * @return a string acting as the contents of the email
     */
    fun createEmailBody(context: Context): String {
        val body = StringBuilder()
        body.append("A new Issue Report has been created:")
        body.append(NEW_LINE_CHAR)
        body.append(NEW_LINE_CHAR)
        body.append(getVersionNames(context))
        body.append(NEW_LINE_CHAR)
        body.append("Description:")
        body.append(NEW_LINE_CHAR)
        body.append(NEW_LINE_CHAR)
        body.append("Estimated system time of occurrence: ")
        body.append(getCurrentTime())
        return body.toString()
    }
}
package com.camgapps.constancia_laboral

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.camgapps.constancia_laboral.BuildConfig
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.File

fun Activity.toast(text: String, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, text, length).show()
}

fun Context.moreApps(): Boolean =  browse("https://play.google.com/store/apps/developer?id=CAMG+APPS")

fun Context.shareApp(text: String = "http://play.google.com/store/apps/details?id=$packageName", subject: String = "Compartir App"): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    try {
        startActivity(Intent.createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        return false
    }
}

fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW) .apply {
            data = Uri.parse(url)
            if (newTask) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
        return true
    } catch (e: Exception) {
        return false
    }

}

fun TextInputLayout.text(): String = this.editText!!.text.toString()

fun Activity.showErrorSnackBar(message: String, errorMessage: Boolean){
    val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    val snackBarView = snackBar.view

    if(errorMessage){
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.error_color))
    }else{
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.success_color))
    }

    if(message.isNotEmpty()){
        snackBar.show()
    }
}

fun String.validateFileName(): Boolean {
    val p = "^[\\w,\\s-]+\\.[A-Za-z]{3}\$".toRegex()
    return matches(p)
}

fun Context.shareFilePDF(path: String): Boolean {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "application/pdf"
    }
    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", File(path)))

    try {
        startActivity(Intent.createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        return false
    }
}


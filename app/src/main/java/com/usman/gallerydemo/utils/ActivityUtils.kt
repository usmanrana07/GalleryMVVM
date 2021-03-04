package com.usman.gallerydemo.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.usman.gallerydemo.R

/**
 * Can show [Toast] from every [Activity].
 */
fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun <T> Activity.launchActivity(
    activityName: Class<T>,
    bundle: Bundle? = null,
    shouldFinish: Boolean = false
) {
    val intent = Intent(this, activityName)
    bundle?.let {
        intent.putExtras(it)
    }
    this.startActivity(intent)
    if (shouldFinish)
        finish()
}

fun Activity.setStatusBarColor(statusBarColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.statusBarColor = statusBarColor
    }
}

fun Activity.showPermissionSettingsConfirmationDialog() {
    val alertDialog = AlertDialog.Builder(this)
        .setTitle(getString(R.string.app_name))
        .setMessage(getString(R.string.message_open_permissions_setting))
        .setPositiveButton(R.string.settings) { dialog, _ ->
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            dialog.dismiss()
        }
        .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
    alertDialog.setCancelable(false)
    alertDialog.show()
}
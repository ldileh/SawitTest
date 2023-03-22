package com.example.sawit.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkPermissionCamera(
    activity: Activity,
    requestCode: Int,
    onGranted: () -> Unit
){
    checkPermission(activity, arrayOf(Manifest.permission.CAMERA), requestCode, onGranted)
}

fun checkPermissionLocation(
    activity: Activity,
    requestCode: Int,
    onGranted: () -> Unit
){
    checkPermission(
        activity,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        ),
        requestCode,
        onGranted
    )
}

fun checkPermission(
    activity: Activity,
    reqPermission: Array<String>,
    requestCode: Int,
    onGranted: () -> Unit,
){
    val needShowRequestPermission = mutableListOf<String>()
    val permissions = mutableListOf<String>()
        .apply {
            reqPermission.forEach { permission ->
                if(
                    ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ){
                    add(permission)
                }
            }
        }
        .also {
            if(it.isNotEmpty()){
                it.forEach { permission ->
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                        needShowRequestPermission.add(permission)
                    }
                }
            }
        }

    when{
        permissions.isEmpty() -> onGranted()

        needShowRequestPermission.isNotEmpty() -> showDialogPermission(activity)

        else -> ActivityCompat.requestPermissions(
            activity,
            permissions.toTypedArray(),
            requestCode
        )
    }
}

fun showDialogPermission(activity: Activity){
    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
    alertDialogBuilder
        .setTitle("Permissions Required")
        .setMessage(
            "You have forcefully denied some of the required permissions " +
                    "for this action. Please open settings, go to permissions and allow them."
        )
        .setPositiveButton("Settings") { _, _ ->
            activity.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
        .setNegativeButton("Cancel") { _, _ -> }
        .setCancelable(false)
        .create()
        .show()
}

fun IntArray.isGrantResultPassed() = this.isNotEmpty() &&
        !this.contains(PackageManager.PERMISSION_DENIED)
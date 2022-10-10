package com.binishmatheww.bubble.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun log( message: String ) = log(tag = "BubBLE", message = message)

fun log( tag: String = "BubBLE", message:String ) = Log.wtf(tag, message)

fun Context.hasPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED else true
}

fun Context.hasPermissions(permissions: Array<String>): Boolean = permissions.all {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED else true
}

fun Activity.requestPermission(permission: String, requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(arrayOf(permission), requestCode)
}

fun Context.hasBluetoothPermission(): Boolean {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED else true
}

fun Context.hasBluetoothConnectPermission(): Boolean {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED else true
}

fun Context.hasBluetoothScanPermission(): Boolean {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED else true
}

fun Context.hasLocationPermission(): Boolean {
    return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
}
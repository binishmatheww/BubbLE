package com.binishmatheww.bubble.views

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binishmatheww.bubble.core.*
import com.binishmatheww.bubble.core.ble.ConnectionEventListener
import com.binishmatheww.bubble.core.ble.ConnectionManager
import com.binishmatheww.bubble.views.screens.DeviceDetailsScreen
import com.binishmatheww.bubble.views.screens.HomeScreen
import com.binishmatheww.bubble.views.screens.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

    private val scanResults = mutableStateListOf<ScanResult>()

    private var selectedGatt = mutableStateOf<BluetoothGatt?>(null)

    private var isBluetoothEnabled = mutableStateOf(false)

    private val hasLocationPermission = mutableStateOf(false)

    private val hasBluetoothScanPermission = mutableStateOf(false)

    private val hasBluetoothConnectPermission = mutableStateOf(false)

    private val isScanning = mutableStateOf(false)

    private val bluetoothStateChangeReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> {
                        isBluetoothEnabled.value = false
                        log("Bluetooth State OFF")
                    }
                    BluetoothAdapter.STATE_ON -> {
                        isBluetoothEnabled.value = true
                        log("Bluetooth State ON")
                    }
                }

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasLocationPermission.value = hasLocationPermission()

        hasBluetoothScanPermission.value = hasBluetoothScanPermission()

        hasBluetoothConnectPermission.value = hasBluetoothConnectPermission()

        lifecycleScope.launch {
            delay(400)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        registerReceiver(
            bluetoothStateChangeReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            }
        )

        setContent {

            Theme.BubbLE {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.welcomeScreen
                ) {

                    composable(route = Routes.welcomeScreen) {

                        WelcomeScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                            onComplete = {
                                navController.popBackStack()
                                navController.navigate(Routes.homeScreen)
                            }
                        )

                    }

                    composable(route = Routes.homeScreen) {

                        HomeScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                            hasBluetoothScanPermission = hasBluetoothScanPermission,
                            hasBluetoothConnectPermission = hasBluetoothConnectPermission,
                            hasLocationPermission = hasLocationPermission,
                            isBluetoothEnabled = isBluetoothEnabled,
                            isScanning = isScanning,
                            scanResults = scanResults,
                            onScanButtonPressed = { shouldScan ->
                                if (shouldScan){
                                    startBleScan()
                                }
                                else{
                                    stopBleScan()
                                }

                            },
                            onDeviceSelected = { scanResult ->

                                with(scanResult.device) {
                                    log("Connecting to $address")
                                    ConnectionManager.connect(this, this@LauncherActivity)
                                    navController.navigate(Routes.deviceDetailsScreen)
                                }

                            }
                        )

                    }

                    composable(route = Routes.deviceDetailsScreen) {

                        DeviceDetailsScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                            gatt = selectedGatt
                        )

                    }

                }

            }

        }

    }


    @SuppressLint("MissingPermission")
    private fun startBleScan() {
        if (
            hasBluetoothScanPermission()
        ) {
            scanResults.clear()
            bleScanner.startScan(null, scanSettings, scanCallback)
            isScanning.value = true
            log("Starting scan...")
        }
    }

    @SuppressLint("MissingPermission")
    private fun stopBleScan() {
        if (
            hasBluetoothScanPermission()
        ) {
            bleScanner.stopScan(scanCallback)
            isScanning.value = false
            scanResults.clear()
            log("Stopping scan...")
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                scanResults[indexQuery] = result
            } else {
                with(result.device) {
                    if (ActivityCompat.checkSelfPermission(
                            this@LauncherActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    log("Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
                }
                scanResults.add(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            log("onScanFailed: code $errorCode")
        }
    }

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                selectedGatt.value = gatt
                ConnectionManager.unregisterListener(this)
            }
            onDisconnect = {

            }
        }
    }

    override fun onResume() {
        super.onResume()

        isBluetoothEnabled.value = bluetoothAdapter.isEnabled
        ConnectionManager.registerListener(connectionEventListener)

    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            unregisterReceiver(bluetoothStateChangeReceiver)
        } catch (e: Exception) {
            log("Error while unregistering bluetoothStateChangeReceiver")
        }

    }

}
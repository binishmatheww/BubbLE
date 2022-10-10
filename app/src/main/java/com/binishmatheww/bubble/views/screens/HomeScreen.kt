package com.binishmatheww.bubble.views.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.binishmatheww.bubble.R
import com.binishmatheww.bubble.core.Theme
import com.binishmatheww.bubble.core.log

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isBluetoothEnabled: MutableState<Boolean>,
    hasBluetoothScanPermission: MutableState<Boolean>,
    hasBluetoothConnectPermission: MutableState<Boolean>,
    hasLocationPermission: MutableState<Boolean>,
    isScanning: MutableState<Boolean>,
    scanResults: SnapshotStateList<ScanResult>,
    onScanButtonPressed: (Boolean) -> Unit,
    onDeviceSelected: (ScanResult) -> Unit,
) {

    Theme.BubbLE {

        ConstraintLayout(
            modifier = modifier
        ) {

            val context = LocalContext.current

            val navigationBarHeight =
                WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

            val notificationBarHeight =
                WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

            val (permissionRationaleConstraint, appNameConstraint, scanResultsTitleConstraint, scanResultsConstraint, bluetoothToggleConstraint) = createRefs()

            val locationPermissionRequestLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

                hasLocationPermission.value = isGranted

            }

            val bluetoothScanPermissionRequestLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

                hasBluetoothScanPermission.value = isGranted

            }

            val bluetoothConnectPermissionRequestLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

                hasBluetoothConnectPermission.value = isGranted

            }

            val enableBluetoothRequestLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    log("Bluetooth request permission result ok")
                } else {
                    log("Bluetooth request permission result canceled / denied")
                }
            }


            if (hasLocationPermission.value) {

                if (hasBluetoothScanPermission.value) {

                    if (hasBluetoothConnectPermission.value) {

                        if (isBluetoothEnabled.value) {

                            Row(
                                modifier = Modifier.constrainAs(appNameConstraint) {
                                    top.linkTo(parent.top, notificationBarHeight.plus(30.dp))
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    color = MaterialTheme.colors.primary,
                                    text = LocalContext.current.getString(R.string.bubbLE),
                                    style = Theme.Typography.bold34
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Image(
                                    modifier = Modifier.size(20.dp),
                                    painter = rememberAsyncImagePainter(model = R.drawable.bluetooth),
                                    contentDescription = LocalContext.current.getString(R.string.bubbLE)
                                )

                            }

                            Text(
                                modifier = Modifier.constrainAs(scanResultsTitleConstraint) {
                                    top.linkTo(appNameConstraint.bottom, 48.dp)
                                    start.linkTo(parent.start, 24.dp)
                                },
                                color = MaterialTheme.colors.primary,
                                text = LocalContext.current.getString(R.string.availableDevices),
                                style = Theme.Typography.bold20
                            )

                            Column(
                                modifier = Modifier.constrainAs(scanResultsConstraint) {
                                    top.linkTo(scanResultsTitleConstraint.bottom, 10.dp)
                                    bottom.linkTo(parent.bottom, navigationBarHeight.plus(30.dp))
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    height = Dimension.fillToConstraints
                                    width = Dimension.fillToConstraints
                                },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                if(isScanning.value){

                                    if(scanResults.isEmpty()){

                                        Text(
                                            color = MaterialTheme.colors.primary,
                                            text = LocalContext.current.getString(R.string.noDevicesAvailable),
                                            style = Theme.Typography.bold14
                                        )

                                    }
                                    else{

                                        LazyColumn(
                                            modifier = Modifier.fillMaxSize(),
                                            content = {

                                                items(scanResults) { scanResult ->
                                                    ScanResult(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(
                                                                top = 4.dp,
                                                                start = 12.dp,
                                                                end = 12.dp
                                                            ),
                                                        scanResult = scanResult,
                                                        onClick = {
                                                            onDeviceSelected.invoke(it)
                                                        }
                                                    )
                                                }

                                            }
                                        )

                                    }

                                }
                                else{

                                    Text(
                                        color = MaterialTheme.colors.primary,
                                        text = LocalContext.current.getString(R.string.startScanToFindNearByDevices),
                                        style = Theme.Typography.bold14
                                    )

                                }

                            }

                            ExtendedFloatingActionButton(
                                modifier = Modifier.constrainAs(bluetoothToggleConstraint) {
                                    end.linkTo(parent.end, 20.dp)
                                    bottom.linkTo(parent.bottom, navigationBarHeight.plus(30.dp))
                                },
                                backgroundColor = MaterialTheme.colors.primary,
                                text = {
                                    Text(
                                        modifier = Modifier.requiredWidth(100.dp),
                                        text = if (isScanning.value) context.getString(R.string.stopScan) else context.getString(
                                            R.string.startScan
                                        )
                                    )
                                },
                                onClick = {
                                    onScanButtonPressed.invoke(!isScanning.value)
                                },
                                icon = {
                                    Icon(Icons.Filled.Bluetooth, "")
                                }
                            )

                        } else {

                            PermissionRationaleScreen(
                                modifier = Modifier
                                    .constrainAs(permissionRationaleConstraint) {
                                        top.linkTo(parent.top, notificationBarHeight.plus(20.dp))
                                        bottom.linkTo(parent.bottom, navigationBarHeight.plus(20.dp))
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                title = LocalContext.current.getString(R.string.youHaveNotEnabledBluetooth),
                                buttonTitle = LocalContext.current.getString(R.string.enable),
                                onClick = {
                                    enableBluetoothRequestLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                                }
                            )
                        }

                    } else {

                        PermissionRationaleScreen(
                            modifier = Modifier
                                .constrainAs(permissionRationaleConstraint) {
                                    top.linkTo(parent.top, notificationBarHeight.plus(20.dp))
                                    bottom.linkTo(parent.bottom, navigationBarHeight.plus(20.dp))
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            title = LocalContext.current.getString(R.string.youHaveNotGivenBluetoothConnectPermission),
                            buttonTitle = LocalContext.current.getString(R.string.grandPermission),
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    bluetoothConnectPermissionRequestLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                                } else {
                                    hasBluetoothScanPermission.value = true
                                }
                            }
                        )

                    }

                } else {

                    PermissionRationaleScreen(
                        modifier = Modifier
                            .constrainAs(permissionRationaleConstraint) {
                                top.linkTo(parent.top, notificationBarHeight.plus(20.dp))
                                bottom.linkTo(parent.bottom, navigationBarHeight.plus(20.dp))
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        title = LocalContext.current.getString(R.string.youHaveNotGivenBluetoothScanPermission),
                        buttonTitle = LocalContext.current.getString(R.string.grandPermission),
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                bluetoothScanPermissionRequestLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
                            } else {
                                hasBluetoothScanPermission.value = true
                            }
                        }
                    )

                }

            } else {

                PermissionRationaleScreen(
                    modifier = Modifier
                        .constrainAs(permissionRationaleConstraint) {
                            top.linkTo(parent.top, notificationBarHeight.plus(20.dp))
                            bottom.linkTo(parent.bottom, navigationBarHeight.plus(20.dp))
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    title = LocalContext.current.getString(R.string.youHaveNotGivenLocationPermission),
                    buttonTitle = LocalContext.current.getString(R.string.grandPermission),
                    onClick = {
                        locationPermissionRequestLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                )

            }

        }

    }

}

@Composable
fun PermissionRationaleScreen(
    modifier: Modifier,
    title: String,
    buttonTitle: String,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title
        )

        Button(
            onClick = {
                onClick.invoke()
            }
        ) {
            Text(
                text = buttonTitle
            )
        }

    }

}

@SuppressLint("MissingPermission")
@Composable
fun ScanResult(
    modifier: Modifier,
    scanResult: ScanResult,
    onClick: (ScanResult) -> Unit
) {

    Column(
        modifier = modifier
            .defaultMinSize(
                minHeight = 50.dp
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                onClick.invoke(scanResult)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            color = MaterialTheme.colors.onPrimary,
            style = Theme.Typography.bold14,
            text = LocalContext.current.getString(R.string.deviceName).plus(" : ").plus(scanResult.device.name ?: LocalContext.current.getString(R.string.unnamed))
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            color = MaterialTheme.colors.onPrimary,
            style = Theme.Typography.medium12,
            text = LocalContext.current.getString(R.string.signalStrength).plus(" : ").plus(scanResult.rssi).plus(" ").plus(LocalContext.current.getString(R.string.dBm))
        )

    }

}
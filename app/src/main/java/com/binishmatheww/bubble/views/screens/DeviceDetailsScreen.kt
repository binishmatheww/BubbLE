package com.binishmatheww.bubble.views.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.binishmatheww.bubble.R
import com.binishmatheww.bubble.core.Theme
import com.binishmatheww.bubble.core.ble.ConnectionManager
import com.binishmatheww.bubble.core.ble.GattUtilities

@SuppressLint("MissingPermission")
@Composable
fun DeviceDetailsScreen(
    modifier: Modifier,
    gatt: MutableState<BluetoothGatt?>
){

    ConstraintLayout(
        modifier = modifier
    ) {

        val navigationBarHeight = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

        val notificationBarHeight = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

        val ( deviceNameConstraint, characteristicsLabelConstraint, characteristicsConstraints ) = createRefs()

        if(gatt.value != null){

            gatt.value?.let { bleGatt ->

                val characteristics by remember{ mutableStateOf(ConnectionManager.servicesOnDevice(bleGatt.device)?.flatMap { it.characteristics ?: listOf() } ?: listOf()) }

                Text(
                    modifier = Modifier.constrainAs(deviceNameConstraint){
                        top.linkTo(parent.top, notificationBarHeight.plus(30.dp))
                        start.linkTo(parent.start, 24.dp)
                    },
                    color = MaterialTheme.colors.primary,
                    text = bleGatt.device?.name ?: LocalContext.current.getString(R.string.unnamed),
                    style = Theme.Typography.bold34
                )

                Text(
                    modifier = Modifier.constrainAs(characteristicsLabelConstraint){
                        top.linkTo(deviceNameConstraint.bottom, 24.dp)
                        start.linkTo(parent.start, 24.dp)
                    },
                    color = MaterialTheme.colors.primary,
                    text = LocalContext.current.getString(R.string.characteristics),
                    style = Theme.Typography.bold14
                )

                LazyColumn(
                    modifier = Modifier.constrainAs(characteristicsConstraints){
                        top.linkTo(characteristicsLabelConstraint.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    },
                    content = {
                        itemsIndexed(characteristics){ index, characteristic ->
                            Characteristic(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = 4.dp,
                                        start = 12.dp,
                                        end = 12.dp
                                    )
                                    .padding(
                                        bottom = if(index == characteristics.lastIndex) navigationBarHeight.plus(24.dp) else 0.dp
                                    ),
                                characteristic = characteristic
                            )
                        }
                    }
                )

            }

        }

    }

}

@Composable
fun Characteristic(
    modifier: Modifier,
    characteristic: BluetoothGattCharacteristic
){

    Column(
        modifier = modifier
            .defaultMinSize(
                minHeight = 50.dp
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            color = MaterialTheme.colors.onPrimary,
            style = Theme.Typography.bold14,
            text = GattUtilities.GattCharacteristics.lookup(characteristic.uuid)?.toString() ?: LocalContext.current.getString(R.string.unnamed)
        )

        /*Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            color = MaterialTheme.colors.onPrimary,
            style = Theme.Typography.medium12,
            text = GattUtilities.GattServices.lookup(characteristic.service.uuid)?.toString() ?: LocalContext.current.getString(R.string.unnamed)
        )*/

    }

}
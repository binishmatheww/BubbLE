package com.binishmatheww.bubble.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.binishmatheww.bubble.R
import com.binishmatheww.bubble.core.Theme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onComplete : () -> Unit
) {

    Theme.BubbLE {

        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible = true
            delay(1000)
            onComplete.invoke()
        }


        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            val painter = rememberAsyncImagePainter(R.drawable.bluetooth)

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 1000)
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 1000)
                )
            ) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painter,
                    contentDescription = "Logidots"
                )
            }

        }

    }

}
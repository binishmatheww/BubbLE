package com.binishmatheww.bubble.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.binishmatheww.bubble.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object Theme {

    @Composable
    fun BubbLE(
        darkTheme : Boolean = isSystemInDarkTheme(),
        content : @Composable () -> Unit
    ) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        val systemUiController = rememberSystemUiController()

        if(darkTheme){
            systemUiController.setSystemBarsColor(
                color = colors.primary,
                darkIcons = false
            )
        }else{
            systemUiController.setSystemBarsColor(
                color = colors.primary,
                darkIcons = true
            )
        }


        MaterialTheme(
            colors = colors,
            typography = Typography(
                h1 = Typography.h1,
                h2 = Typography.h2,
                h3 = Typography.h3,
                h4 = Typography.h4,
                h5 = Typography.h5,
                h6 = Typography.h6,
                subtitle1 = Typography.subtitle1,
                subtitle2 = Typography.subtitle2,
                body1 = Typography.body1,
                body2 = Typography.body2,
                button = Typography.button,
                caption = Typography.caption,
                overline = Typography.overline
            ),
            shapes = shapes,
            content = content
        )

    }


    private val DarkColorPalette = darkColors(
        primary = ColorPalette.linkWater,
        primaryVariant = ColorPalette.titanWhite,
        //secondary = ColorPalette.nevada,
        //secondaryVariant = ColorPalette.shuttleGray,
        background = ColorPalette.nevada,
        surface = ColorPalette.nevada,
        onPrimary = ColorPalette.nevada,
        //onSecondary = ColorPalette.linkWater,
        onSurface = ColorPalette.linkWater,
        onBackground = ColorPalette.linkWater,
        onError = ColorPalette.linkWater,
    )

    private val LightColorPalette = lightColors(
        primary = ColorPalette.nevada,
        primaryVariant = ColorPalette.shuttleGray,
        //secondary = ColorPalette.nevada,
        //secondaryVariant = ColorPalette.shuttleGray,
        background = ColorPalette.linkWater,
        surface = ColorPalette.linkWater,
        onPrimary = ColorPalette.linkWater,
        //onSecondary = ColorPalette.nevada,
        onSurface = ColorPalette.nevada,
        onBackground = ColorPalette.nevada,
        onError = ColorPalette.nevada,
    )

    private val shapes = Shapes(
        small = RoundedCornerShape(2.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(8.dp)
    )


    object ColorPalette{

        val linkWater = Color(0xFFE6F0FA)

        val titanWhite = Color(0xFFE2E2FF)

        val nevada = Color(0xFF676E74)

        val shuttleGray = Color(0xFF5D5F61)

    }

    object Typography {

        val raleway = FontFamily(
            Font(R.font.raleway_light, FontWeight.Light),
            Font(R.font.raleway_medium, FontWeight.Medium),
            Font(R.font.raleway_bold, FontWeight.Bold)
        )

        val h1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 56.sp
        )

        val h2 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 48.sp
        )

        val h3 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        )
        val h4 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp
        )

        val h5 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )

        val h6 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        val subtitle1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 26.sp
        )
        val subtitle2 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        val body1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
        val body2 = TextStyle(
            fontFamily = raleway,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        val button = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 4.sp
        )

        val caption = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val overline = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )

        val bold34 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )

        val bold24 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        val bold20 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        val bold14 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        val bold12 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

        val medium20 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        )

        val medium16 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        val medium14 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        val medium12 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )

    }

}
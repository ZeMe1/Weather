package kz.zeme.weather.shared.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
private fun weatherFontFamily(): FontFamily = FontFamily(
    Font(R.font.sf_pro_100, weight = FontWeight.W100),
    Font(R.font.sf_pro_300, weight = FontWeight.W300),
    Font(R.font.sf_pro_500, weight = FontWeight.W500),
    Font(R.font.sf_pro_700, weight = FontWeight.W700),
    Font(R.font.sf_compact_500, weight = FontWeight.W500)
)



@Immutable
data class WeatherTypography(
    val weight100Size92LineHeight83: TextStyle,
    val weight300Size48LineHeight48: TextStyle,
    val weight500Size10LineHeight10: TextStyle,
    val weight500Size15LineHeight21: TextStyle,
    val weight500Size18LineHeight21: TextStyle,
    val weight500Size20LineHeight20: TextStyle,
    val weight500Size32LineHeight32: TextStyle,
    val weight700Size10LineHeight10: TextStyle,
    val weight700Size32LineHeight32: TextStyle,
)

@Composable
fun defaultWeatherTypography(): WeatherTypography {
    val fontFamily = weatherFontFamily()
    return WeatherTypography(
        weight100Size92LineHeight83 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W100,
            fontSize = 92.sp,
            lineHeight = 83.sp
        ),
        weight300Size48LineHeight48 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W300,
            fontSize = 48.sp,
            lineHeight = 48.sp
        ), weight500Size10LineHeight10 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 10.sp,
            lineHeight = 10.sp
        ), weight500Size15LineHeight21 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            lineHeight = 21.sp
        ), weight500Size18LineHeight21 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            lineHeight = 21.sp
        ), weight500Size20LineHeight20 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 20.sp,
            lineHeight = 20.sp
        ), weight500Size32LineHeight32 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 32.sp,
            lineHeight = 32.sp
        ), weight700Size10LineHeight10 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 10.sp,
            lineHeight = 10.sp
        ), weight700Size32LineHeight32 =TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 32.sp,
            lineHeight = 32.sp
        )
    )
}

val LocalWeatherTypography = staticCompositionLocalOf<WeatherTypography>{ error("No default Typography provided") }
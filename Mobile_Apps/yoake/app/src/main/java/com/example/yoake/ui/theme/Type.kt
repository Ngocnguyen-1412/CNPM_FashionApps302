package com.example.yoake.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Font families ────────────────────────────────────────────────────────────
// Add the actual .ttf/.otf files to res/font/ and reference them by R.font.<name>.
// Example:  Font(R.font.noto_serif_regular, FontWeight.Normal)
// Until then these are declared as system-font fallbacks so the project compiles.

val NotoSerif = FontFamily.Serif   // replace with: FontFamily(Font(R.font.noto_serif_regular, FontWeight.Normal), ...)
val Inter     = FontFamily.SansSerif // replace with: FontFamily(Font(R.font.inter_regular, FontWeight.Normal), ...)

// ─── Type scale (maps display-lg / display-md / headline-sm / body-lg / body-md / caption / label-caps) ──
val YoakeTypography = Typography(

    // display-lg  → 32sp, Noto Serif, weight 400, ls -0.02em
    displayLarge = TextStyle(
        fontFamily = NotoSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.64).sp
    ),

    // display-md  → 24sp, Noto Serif, weight 400, ls -0.01em
    displayMedium = TextStyle(
        fontFamily = NotoSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 31.sp,
        letterSpacing = (-0.24).sp
    ),

    // headline-sm → 20sp, Noto Serif, weight 500
    headlineSmall = TextStyle(
        fontFamily = NotoSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),

    // body-lg → 16sp, Inter, weight 400, ls 0.01em
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.16.sp
    ),

    // body-md → 14sp, Inter, weight 400
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),

    // caption → 12sp, Inter, weight 400
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp
    ),

    // label-caps → 12sp, Inter, weight 600, ls 0.08em (used for buttons / tags)
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.96.sp
    )
)

package com.example.myapplication.giao_dien

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.PinkPrimary
import kotlinx.coroutines.delay

@Composable
fun ManHinhChao(onNavigateToNext: () -> Unit) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        onNavigateToNext()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(PinkPrimary)
    ) {
        LogoN3h2(
            modifier = Modifier.scale(scale.value)
        )
    }
}

// Helper class for animation easing similar to Android's OvershootInterpolator
class OvershootInterpolator(private val tension: Float = 2f) {
    fun getInterpolation(t: Float): Float {
        var tCopy = t
        tCopy -= 1.0f
        return tCopy * tCopy * ((tension + 1) * tCopy + tension) + 1.0f
    }
}

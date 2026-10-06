package com.caloout.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.caloout.calculator.ui.CalorieCalculatorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloOutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalorieCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CaloOutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF0284C7),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE0F2FE),
        onPrimaryContainer = Color(0xFF0369A1),
        secondary = Color(0xFF0D9488),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFCCFBF1),
        onSecondaryContainer = Color(0xFF0F766E),
        tertiary = Color(0xFF16A34A),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFDCFCE7),
        background = Color(0xFFF8FAFC),
        surface = Color.White,
        surfaceVariant = Color(0xFFF1F5F9),
        onSurface = Color(0xFF0F172A),
        onSurfaceVariant = Color(0xFF64748B)
    )

    val darkColors = darkColorScheme(
        primary = Color(0xFF38BDF8),
        onPrimary = Color(0xFF082F49),
        primaryContainer = Color(0xFF0C4A6E),
        onPrimaryContainer = Color(0xFFE0F2FE),
        secondary = Color(0xFF2DD4BF),
        onSecondary = Color(0xFF042F2E),
        secondaryContainer = Color(0xFF134E4A),
        onSecondaryContainer = Color(0xFFCCFBF1),
        tertiary = Color(0xFF4ADE80),
        onTertiary = Color(0xFF052E16),
        tertiaryContainer = Color(0xFF14532D),
        background = Color(0xFF0F172A),
        surface = Color(0xFF1E293B),
        surfaceVariant = Color(0xFF334155),
        onSurface = Color(0xFFF8FAFC),
        onSurfaceVariant = Color(0xFF94A3B8)
    )

    val colorScheme = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

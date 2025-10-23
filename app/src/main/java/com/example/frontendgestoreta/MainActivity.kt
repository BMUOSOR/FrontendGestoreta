package com.example.frontendgestoreta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontendgestoreta.ui.screens_user.*
import com.example.frontendgestoreta.ui.screens_gestor.*
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontendGestoretaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    MainScreenGestor()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FrontendGestoretaTheme {
        MainScreen()
    }
}
package com.udb.convite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.udb.convite.ui.navigation.ConviteNavGraph
import com.udb.convite.ui.theme.ConviteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConviteTheme {
                ConviteNavGraph()
            }
        }
    }
}

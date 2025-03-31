package com.ulatina.proyectoprogra5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ulatina.proyectoprogra5.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import utn.swdm.items.ui.theme.ItemsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContent {
            ItemsTheme {
                Navigation()
            }
        }
    }
}
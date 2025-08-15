package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.HomeScreen
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.ui.RegisterScreen
import com.example.myapplication.ui.ResetPasswordScreen
import com.example.myapplication.ui.ReviewScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold (){
//                    HomeScreen(
//                        modifier = Modifier.padding(it)
//                    )
                    // RegisterScreen(
                    // modifier = Modifier.padding(it)
                    // )
                    // LoginScreen(
                    // modifier = Modifier.padding(it)
                    // )
                    // ResetPasswordScreen(
                    // modifier = Modifier.padding(it)
                    // )
//                   ReviewScreen(
//                       modifier = Modifier.padding(it)
//                   )
                    ProfileScreen(
                        modifier = Modifier.padding(it)
                    )
                }

            }


        }
    }
}
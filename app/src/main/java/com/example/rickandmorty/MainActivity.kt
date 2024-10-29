package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.ui.screens.CharacterDetailScreen
import com.example.rickandmorty.ui.screens.HomeScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            RickAndMortyTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(navController = navController, startDestination = "home"){
                        composable(route = "home"){
                            HomeScreen(innerPadding, navController) // recibe y hace la navegacion
                        }
                        composable(
                            route = "detail/{id}",
                            arguments = listOf(
                                navArgument("id"){
                                    type = NavType.IntType
                                    nullable = false
                                }
                            )
                        ){
                            val id = it.arguments?.getInt("id") ?: 0
                            CharacterDetailScreen(
                                id = id,
                                innerPaddingValues = innerPadding,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
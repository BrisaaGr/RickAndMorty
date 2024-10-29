package com.example.rickandmorty.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rickandmorty.models.Character
import com.example.rickandmorty.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip


@Composable
fun HomeScreen(innerPadding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf(listOf<Character>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val characterService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CharacterService::class.java)

                val response = characterService.getCharacters()
                characters = response.results
                Log.i("HomeScreenResponse", characters.toString())
            } catch (e: Exception) {
                characters = listOf()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF4CAF50))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF393E46), Color(0xFF00ADB5))
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Rick & Morty Universe",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp)
                    .graphicsLayer { shadowElevation = 8.dp.toPx() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(characters) { character ->
                    CharacterCard(character = character, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${character.id}") }
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF222831))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF00ADB5), Color(0xFF222831))))
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = character.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Species: ${character.species}",
                fontSize = 14.sp,
                color = Color(0xFFBDBDBD)
            )
        }
    }
}

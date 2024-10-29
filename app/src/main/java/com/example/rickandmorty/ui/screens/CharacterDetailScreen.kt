package com.example.rickandmorty.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.rickandmorty.models.Character
import com.example.rickandmorty.models.Location
import com.example.rickandmorty.models.Origin
import com.example.rickandmorty.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.navigation.NavController

@Composable
fun CharacterDetailScreen(id: Int, innerPaddingValues: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var character by remember {
        mutableStateOf(Character(
            id = 0, name = "", image = "", status = "", created = "",
            episode = emptyList(), gender = "", location = Location(name = "", url = ""),
            origin = Origin(name = "", url = ""), species = "", type = "", url = ""
        ))
    }

    LaunchedEffect(key1 = true) {
        scope.launch {
            val BASE_URL = "https://rickandmortyapi.com/api/"
            val characterService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharacterService::class.java)

            isLoading = true
            character = characterService.getCharacterById(id)
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF4CAF50))
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF222831), Color(0xFF00ADB5), Color(0xFF393E46))
                    )
                ),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Botón de retroceso en la esquina superior izquierda
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color(0xFF00ADB5).copy(alpha = 0.8f))
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_media_previous),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Contenedor de imagen e información
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPaddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Cuadro contenedor
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(16.dp)
                            .fillMaxWidth(0.9f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Imagen del personaje
                            Image(
                                painter = rememberAsyncImagePainter(character.image),
                                contentDescription = "Character Image",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.3f))
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Nombre del personaje
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFECECEC)
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Detalles con fondo y transparencia
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF333A42).copy(alpha = 0.7f))
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                DetailTextWithIcon(Icons.Filled.Person, "Status", character.status)
                                DetailTextWithIcon(Icons.Filled.Person, "Species", character.species)
                                DetailTextWithIcon(Icons.Filled.Person, "Gender", character.gender)
                                DetailTextWithIcon(Icons.Filled.LocationOn, "Location", character.location?.name ?: "Unknown")
                                DetailTextWithIcon(Icons.Filled.LocationOn, "Origin", character.origin?.name ?: "Unknown")
                                DetailTextWithIcon(Icons.Filled.Person, "Created", character.created)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetailTextWithIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = "$label Icon",
            tint = Color(0xFF00ADB5),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFECECEC)
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFFBDBDBD)
                )
            )
        }
    }
}

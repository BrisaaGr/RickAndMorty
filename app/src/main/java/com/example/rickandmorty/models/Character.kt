package com.example.rickandmorty.models

data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
){
    val computedName get() = if(name.length> 11) "${name.substring(0,11)}..." else name
}
package com.example.rickandmorty.models

data class ApiResponse(
    val info: Info,
    val results: List<Character>
)
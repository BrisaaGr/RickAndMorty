package com.example.rickandmorty.services

import com.example.rickandmorty.models.ApiResponse
import com.example.rickandmorty.models.Character
import retrofit2.http.GET
import retrofit2.http.Path


interface CharacterService {
        @GET("character")
        suspend fun getCharacters(): ApiResponse

        @GET("character/{id}")
        suspend fun getCharacterById(@Path("id") id: Int):Character
}
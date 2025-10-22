package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto

interface GastroBarRemoteDataSource {
    suspend fun getAllGastroBares(): List<GastroBarDto>
    suspend fun getGastroBarById(id: String): GastroBarDto
    suspend fun createGastroBar(gastrobar: CreateGastroBarDto)
    suspend fun updateGastroBar(id: String, gastrobar: CreateGastroBarDto)
    suspend fun deleteGastroBar(id: String)
}

package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto

interface GastroBarRemoteDataSource {
    suspend fun getAllGastroBares(): List<GastroBarDto>
    suspend fun getGastroBarById(id: Int): GastroBarDto
    suspend fun createGastroBar(gastrobar: CreateGastroBarDto)
    suspend fun updateGastroBar(id: Int, gastrobar: CreateGastroBarDto)
    suspend fun deleteGastroBar(id: Int)
}

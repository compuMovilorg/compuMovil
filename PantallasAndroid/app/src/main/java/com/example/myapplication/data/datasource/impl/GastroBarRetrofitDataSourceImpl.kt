package com.example.myapplication.data.datasource.impl

import com.example.myapplication.data.datasource.GastroBarRemoteDataSource
import com.example.myapplication.data.datasource.services.GastroBarRetrofitService
import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto
import javax.inject.Inject

class GastroBarRetrofitDataSourceImpl @Inject constructor(
    private val service: GastroBarRetrofitService
) : GastroBarRemoteDataSource {
    override suspend fun getAllGastroBares(): List<GastroBarDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getGastroBarById(id: String): GastroBarDto {
        TODO("Not yet implemented")
    }

    override suspend fun createGastroBar(gastrobar: CreateGastroBarDto) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGastroBar(
        id: String,
        gastrobar: CreateGastroBarDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGastroBar(id: String) {
        TODO("Not yet implemented")
    }
}

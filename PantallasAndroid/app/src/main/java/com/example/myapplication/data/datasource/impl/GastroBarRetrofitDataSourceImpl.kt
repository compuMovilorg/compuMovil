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
        return service.getAllGastroBares()
    }

    override suspend fun getGastroBarById(id: Int): GastroBarDto {
        return service.getGastroBarById(id)
    }

    override suspend fun createGastroBar(gastrobar: CreateGastroBarDto) {
        service.createGastroBar(gastrobar)
    }

    override suspend fun updateGastroBar(id: Int, gastrobar: CreateGastroBarDto) {
        service.updateGastroBar(id, gastrobar)
    }

    override suspend fun deleteGastroBar(id: Int) {
        service.deleteGastroBar(id)
    }
}

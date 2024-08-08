package com.gestionSupermercados.supermercado

object SupermercadoProvider {
    private val supermercadoRepository: SupermercadoRepository by lazy {
        SupermercadoRepositoryImpl()
    }

    fun getSupermercadoService(): SupermercadoService {
        return SupermercadoServiceImpl(supermercadoRepository)
    }
}
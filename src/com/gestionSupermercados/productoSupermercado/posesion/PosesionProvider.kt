package com.gestionSupermercados.productoSupermercado.posesion

object PosesionProvider {
    private val posesionRepository: PosesionRepository by lazy {
        PosesionRepositoryImpl()
    }

    fun getPosesionService(): PosesionService {
        return PosesionService(posesionRepository)
    }
}
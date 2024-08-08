package com.gestionSupermercados.productoSupermercado.posesion

import java.util.*

interface PosesionRepository {
    fun getPosesionByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Posesion?
    fun addPosesion(posesion: Posesion)
}

class PosesionRepositoryImpl : PosesionRepository {
    private val productosSupermercado = mutableListOf<Posesion>()

    override fun getPosesionByProductoIdSupermercadoId(productoId : UUID, supermercadoId: UUID) : Posesion? {
        return productosSupermercado.find { it.productoId == productoId && it.supermercadoId == supermercadoId}
    }

    override fun addPosesion(posesion : Posesion) {
        productosSupermercado.add(posesion)
    }
}
package com.gestionSupermercados.productoSupermercado.posesion

import java.util.*

class PosesionRepository {
    private val productosSupermercado = mutableListOf<Posesion>()

    fun getPosesionByProductoIdSupermercadoId(productoId : UUID, supermercadoId: UUID) : Posesion? {
        return productosSupermercado.find { it.productoId == productoId && it.supermercadoId == supermercadoId}
    }

    fun addPosesion(posesion : Posesion) {
        productosSupermercado.add(posesion)
    }
}
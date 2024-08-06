package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.producto.Producto

class PosesionRepository {
    private val productosSupermercado = mutableListOf<Posesion>()

    fun getPosesionByProductoIdSupermercadoId(productoId : Long, supermercadoId: Long) : Posesion? {
        return productosSupermercado.find { it.productoId == productoId && it.supermercadoId == supermercadoId}
    }

    fun addPosesion(posesion : Posesion) {
        productosSupermercado.add(posesion)
    }

    fun updatePosesionStockByProductoIdSupermercadoId(productoId : Long, supermercadoId : Long, difference : Int) {
        val posesionAModificar : Posesion? = getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
        if (posesionAModificar != null) posesionAModificar.stock += difference
    }
}
package com.gestionSupermercados.producto

import java.util.UUID

class ProductoRepository {
    private val productos = mutableListOf<Producto>()

    fun getProductoById(id : UUID) : Producto? {
        return productos.find { it.id == id }
    }

    fun addProducto(producto : Producto) {
        productos.add(producto)
    }
}
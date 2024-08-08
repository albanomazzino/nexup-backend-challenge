package com.gestionSupermercados.producto

import java.util.UUID

interface ProductoRepository {
    fun getProductoById(id: UUID): Producto?
    fun addProducto(producto: Producto)
}

class ProductoRepositoryImpl : ProductoRepository {
    private val productos = mutableListOf<Producto>()

    override fun getProductoById(id : UUID) : Producto? {
        return productos.find { it.id == id }
    }

    override fun addProducto(producto : Producto) {
        productos.add(producto)
    }
}
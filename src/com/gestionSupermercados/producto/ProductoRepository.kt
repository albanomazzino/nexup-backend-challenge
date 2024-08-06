package com.gestionSupermercados.producto

class ProductoRepository {
    private val productos = mutableListOf<Producto>()

    fun getProductoById(id : Long) : Producto? {
        return productos.find { it.id == id }
    }

    fun addProducto(producto : Producto) {
        productos.add(producto)
    }
}
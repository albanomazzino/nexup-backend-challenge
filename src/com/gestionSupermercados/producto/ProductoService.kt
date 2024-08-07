package com.gestionSupermercados.producto

import java.util.*

class ProductoService (private val productoRepository: ProductoRepository) {
    fun addProducto(producto: Producto) {
        productoRepository.addProducto(producto)
    }

    fun getProductoById(id: UUID): Producto? {
        return productoRepository.getProductoById(id)
    }
}
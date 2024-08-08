package com.gestionSupermercados.producto

import java.util.*

interface ProductoService {
    fun addProducto(producto: Producto)
    fun getProductoById(id: UUID): Producto
}

class ProductoServiceImpl (private val productoRepository: ProductoRepository) : ProductoService {
    override fun addProducto(producto: Producto) {
        productoRepository.addProducto(producto)
    }

    override fun getProductoById(id: UUID): Producto {
        return productoRepository.getProductoById(id)
            ?: throw NoSuchElementException("Producto con ID $id no encontrado.")
    }
}
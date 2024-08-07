package com.gestionSupermercados.producto

class ProductoService (private val productoRepository: ProductoRepository) {
    fun addProducto(producto: Producto) {
        productoRepository.addProducto(producto)
    }

    fun getProductoById(id: Long): Producto? {
        return productoRepository.getProductoById(id)
    }
}
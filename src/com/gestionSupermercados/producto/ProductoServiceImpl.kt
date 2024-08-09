package com.gestionSupermercados.producto

import com.gestionSupermercados.ConstantValues
import java.util.*

/**
 * Interfaz para el servicio de gestión de productos.
 */
interface ProductoService {
    /**
     * Añade un nuevo producto.
     *
     * @param producto El producto a añadir.
     */
    fun addProducto(producto: Producto)

    /**
     * Obtiene un producto por su ID.
     *
     * @param id Id del producto a buscar.
     * @return El producto correspondiente al id proporcionado.
     * @throws NoSuchElementException Si no se encuentra el producto con el id proporcionado.
     */
    fun getProductoById(id: UUID): Producto
}

class ProductoServiceImpl (private val productoRepository: ProductoRepository) : ProductoService {
    override fun addProducto(producto: Producto) {
        validarProducto(producto)
        productoRepository.addProducto(producto)
    }

    override fun getProductoById(id: UUID): Producto {
        return productoRepository.getProductoById(id)
            ?: throw NoSuchElementException(String.format(ConstantValues.PRODUCTO_NO_ENCONTRADO_MESSAGE, id))
    }

    private fun validarProducto(producto: Producto) {
        if (producto.nombre.isBlank()) {
            throw IllegalArgumentException("El nombre del producto no puede estar vacío.")
        }
        if (producto.precio <= 0.0) {
            throw IllegalArgumentException("El precio del producto debe ser mayor a 0.")
        }
    }
}
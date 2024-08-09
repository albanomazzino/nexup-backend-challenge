package com.gestionSupermercados.producto

import java.util.UUID

/**
 * Interfaz para la gestión de productos en el repositorio.
 */
interface ProductoRepository {
    /**
     * Obtiene un producto por su ID.
     *
     * @param id Id del producto a buscar.
     * @return El producto correspondiente al ID proporcionado.
     */
    fun getProductoById(id: UUID): Producto?

    /**
     * Añade un nuevo producto.
     *
     * @param producto El producto a añadir.
     */
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
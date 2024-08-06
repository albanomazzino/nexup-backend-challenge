package com.gestionSupermercados.producto

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductoRepositoryTest {

    private lateinit var productoRepository : ProductoRepository
    /**
     * Datos de prueba:
     *  # Productos [id, nombre, precio]
     *      - 1, "Carne", 10.0
     *      - 2, "Pescado", 20.0
     *      - 3, "Pollo", 30.0
     *      - 4, "Cerdo", 45.0
     *      - 5, "Ternera", 50.0
     *      - 6, "Cordero", 65.0
     * */

    @BeforeEach
    fun setUp() {
        productoRepository = ProductoRepository()
    }

    @Test
    fun addProductoTest() {
        val producto = Producto(1, "Carne", 10.0)
        productoRepository.addProducto(producto)

        val addedProducto = productoRepository.getProductoById(1)

        assertNotNull(addedProducto)
        assertEquals(producto, addedProducto)
    }
}





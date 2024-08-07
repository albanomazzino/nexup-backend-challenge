package com.gestionSupermercados.producto

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductoRepositoryTest {

    private lateinit var productoRepository : ProductoRepository

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





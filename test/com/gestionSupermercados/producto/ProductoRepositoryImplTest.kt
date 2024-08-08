package com.gestionSupermercados.producto

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProductoRepositoryImplTest {
    private lateinit var productoRepository : ProductoRepository

    @BeforeEach
    fun setUp() {
        productoRepository = ProductoRepositoryImpl()
    }

    @Test
    fun getProductoWithNoOccurrencesTest() {
        val addedProducto = productoRepository.getProductoById(UUID.randomUUID())

        assertNull(addedProducto)
    }

    @Test
    fun addProductoTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        productoRepository.addProducto(producto)

        val addedProducto = productoRepository.getProductoById(ConstantValues.testProducto1)

        assertNotNull(addedProducto)
        assertEquals(producto, addedProducto)
    }
}





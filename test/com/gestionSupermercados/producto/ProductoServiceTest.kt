package com.gestionSupermercados.producto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductoServiceTest {

    private lateinit var productoService: ProductoService
    private lateinit var productoRepository: ProductoRepository

    @BeforeEach
    fun setUp() {
        productoRepository = ProductoRepository()
        productoService = ProductoService(productoRepository)
    }

    @Test
    fun addProductoTest() {
        val producto = Producto(1, "Carne", 10.0)
        productoService.addProducto(producto)

        val productoEncontrado = productoService.getProductoById(1)
        assertEquals(producto, productoEncontrado)
    }

    @Test
    fun getProductoByIdTest() {
        val producto1 = Producto(1, "Carne", 10.0)
        val producto2 = Producto(2, "Pescado", 20.0)
        productoService.addProducto(producto1)
        productoService.addProducto(producto2)

        val productoEncontrado1 = productoService.getProductoById(1)
        val productoEncontrado2 = productoService.getProductoById(2)

        assertEquals(producto1, productoEncontrado1)
        assertEquals(producto2, productoEncontrado2)
    }

    @Test
    fun getProductoByIdNotFoundTest() {
        val productoEncontrado = productoService.getProductoById(999)

        assertNull(productoEncontrado)
    }
}
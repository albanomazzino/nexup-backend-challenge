package com.gestionSupermercados.producto

import com.gestionSupermercados.ConstVals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

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
        val producto = Producto(ConstVals.testProducto1, "Carne", 10.0)
        productoService.addProducto(producto)

        val productoEncontrado = productoService.getProductoById(ConstVals.testProducto1)

        assertEquals(producto, productoEncontrado)
    }

    @Test
    fun getProductoByIdTest() {
        val producto1 = Producto(ConstVals.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstVals.testProducto2, "Pescado", 20.0)
        productoService.addProducto(producto1)
        productoService.addProducto(producto2)

        val productoEncontrado1 = productoService.getProductoById(ConstVals.testProducto1)
        val productoEncontrado2 = productoService.getProductoById(ConstVals.testProducto2)

        assertEquals(producto1, productoEncontrado1)
        assertEquals(producto2, productoEncontrado2)
    }

    @Test
    fun getProductoByIdNotFoundTest() {
        val productoEncontrado = productoService.getProductoById(UUID.randomUUID())

        assertNull(productoEncontrado)
    }
}
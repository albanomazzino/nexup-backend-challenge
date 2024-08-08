package com.gestionSupermercados.producto

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
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
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        productoService.addProducto(producto)

        val productoEncontrado = productoService.getProductoById(ConstantValues.testProducto1)

        assertEquals(producto, productoEncontrado)
    }

    @Test
    fun getProductoByIdTest() {
        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        productoService.addProducto(producto1)
        productoService.addProducto(producto2)

        val productoEncontrado1 = productoService.getProductoById(ConstantValues.testProducto1)
        val productoEncontrado2 = productoService.getProductoById(ConstantValues.testProducto2)

        assertEquals(producto1, productoEncontrado1)
        assertEquals(producto2, productoEncontrado2)
    }

    @Test
    fun getProductoByIdWithNoOccurrencesTest() {
        val testId = UUID.randomUUID()
        val exception = assertThrows(NoSuchElementException::class.java) {
            productoService.getProductoById(testId)
        }

        assertEquals("Producto con ID $testId no encontrado.", exception.message)
    }
}
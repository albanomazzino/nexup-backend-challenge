package com.gestionSupermercados.producto

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class ProductoServiceImplTest {
    private val productoRepository = mock(ProductoRepository::class.java)
    private lateinit var productoService: ProductoService

    @BeforeEach
    fun setUp() {
        productoService = ProductoServiceImpl(productoRepository)
    }

    @Test
    fun addProductoTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)

        `when`(productoRepository.getProductoById(ConstantValues.testProducto1))
            .thenReturn(producto)

        doNothing().`when`(productoRepository).addProducto(producto)
        productoService.addProducto(producto)

        val productoEncontrado = productoService.getProductoById(ConstantValues.testProducto1)

        assertEquals(producto, productoEncontrado)
    }

    @Test
    fun addProductoWithEmptyNameThrowsException() {
        val producto = Producto(ConstantValues.testProducto1, "", 10.0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            productoService.addProducto(producto)
        }

        assertEquals("El nombre del producto no puede estar vacío.", exception.message)
        verify(productoRepository, never()).addProducto(producto)
    }

    @Test
    fun addProductoWithNegativePriceThrowsException() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 0.0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            productoService.addProducto(producto)
        }

        assertEquals("El precio del producto debe ser mayor a 0.", exception.message)
        verify(productoRepository, never()).addProducto(producto)
    }

    @Test
    fun getProductoByIdTest() {
        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)

        `when`(productoRepository.getProductoById(ConstantValues.testProducto1))
            .thenReturn(producto1)
        `when`(productoRepository.getProductoById(ConstantValues.testProducto2))
            .thenReturn(producto2)

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

        assertEquals(productoNoEncontradoMessage(testId), exception.message)
    }

    private fun productoNoEncontradoMessage(productoId: UUID): String {
        return String.format(ConstantValues.PRODUCTO_NO_ENCONTRADO_MESSAGE, productoId)
    }
}
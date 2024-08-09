package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.*
import com.gestionSupermercados.supermercado.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class PosesionServiceImplTest {
    private val posesionRepository = mock(PosesionRepository::class.java)
    private val productoRepository = mock(ProductoRepository::class.java)
    private val supermercadoRepository = mock(SupermercadoRepository::class.java)
    private lateinit var posesionService: PosesionService

    @BeforeEach
    fun setUp() {
        posesionService = PosesionServiceImpl(posesionRepository)
    }

    @Test
    fun getStockWithNoOccurrencesTest() {
        val testProductoId = UUID.randomUUID()
        val testSupermercadoId = UUID.randomUUID()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.getStock(testProductoId, testSupermercadoId)
        }

        assertEquals(ConstantValues.PRODUCTO_O_SUPERMERCADO_NO_ENCONTRADO_MESSAGE, exception.message)
    }

    @Test
    fun getStockTest() {
        val producto = crearProducto()
        val supermercado = crearSupermercado()
        val stockEsperado = 50

        `when`(productoRepository.getProductoById(ConstantValues.testProducto1))
            .thenReturn(producto)
        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1))
            .thenReturn(supermercado)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockEsperado))

        val stock = posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(stockEsperado, stock)
        verify(posesionRepository).getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
        verifyNoMoreInteractions(posesionRepository)
    }

    @Test
    fun addPosesionTest() {
        val testPosesion = Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        doNothing().`when`(posesionRepository).addPosesion(testPosesion)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(testPosesion)
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(testPosesion, posesion)
        verify(posesionRepository).addPosesion(testPosesion)
    }

    @Test
    fun addPosesionWithNegativeStock() {
        val stockNegativo = -10
        val testPosesion = Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockNegativo)

        doNothing().`when`(posesionRepository).addPosesion(testPosesion)
        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(testPosesion)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockNegativo)
        }

        assertEquals(ConstantValues.STOCK_NEGATIVO_MESSAGE, exception.message)
        verify(posesionRepository, never()).addPosesion(testPosesion)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val stockInicial = 50
        val stockActualizado = 30

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockInicial)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockInicial))
        posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -20)

        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(stockActualizado, posesion?.stock)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdWithNegativeStockTest() {
        val stockInicial = 50

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockInicial)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, stockInicial))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -51)
        }

        assertEquals(ConstantValues.STOCK_NEGATIVO_MESSAGE, exception.message)
    }

    private fun crearProducto(): Producto {
        return Producto(ConstantValues.testProducto1, "Carne", 10.0)
    }

    private fun crearSupermercado(): Supermercado {
        return Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
    }
}

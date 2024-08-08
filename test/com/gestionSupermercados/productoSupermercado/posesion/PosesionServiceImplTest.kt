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

    private lateinit var posesionService: PosesionService
    private lateinit var posesionRepository: PosesionRepository
    private lateinit var productoService: ProductoService
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var productoRepository: ProductoRepository
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        posesionRepository = mock(PosesionRepository::class.java)
        productoRepository = mock(ProductoRepository::class.java)
        supermercadoRepository = mock(SupermercadoRepository::class.java)
        productoService = mock(ProductoService::class.java)
        supermercadoService = mock(SupermercadoService::class.java)
        posesionService = PosesionServiceImpl(posesionRepository)
    }

    @Test
    fun getStockWithNoOccurrencesTest() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.getStock(UUID.randomUUID(), ConstantValues.testSupermercado1)
        }

        assertEquals("Producto o supermercado no encontrado.", exception.message)
    }

    @Test
    fun getStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        `when`(productoRepository.getProductoById(ConstantValues.testProducto1))
            .thenReturn(producto)
        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1))
            .thenReturn(supermercado)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50))

        val stock = posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(50, stock)
    }

    @Test
    fun addPosesionTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val testPosesion = Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        doNothing().`when`(productoService).addProducto(producto)
        doNothing().`when`(supermercadoService).addSupermercado(supermercado)
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        doNothing().`when`(posesionRepository).addPosesion(testPosesion)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(testPosesion)
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(posesion, testPosesion)
        verify(posesionRepository).addPosesion(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50))
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        doNothing().`when`(productoService).addProducto(producto)
        doNothing().`when`(supermercadoService).addSupermercado(supermercado)
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50))
        posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -20)

        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(30, posesion?.stock)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdWithNegativeStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        doNothing().`when`(productoService).addProducto(producto)
        doNothing().`when`(supermercadoService).addSupermercado(supermercado)
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        `when`(posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
            .thenReturn(Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50))
        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -51)
        }
        assertEquals("El stock no puede ser menor a 0.", exception.message)
    }
}

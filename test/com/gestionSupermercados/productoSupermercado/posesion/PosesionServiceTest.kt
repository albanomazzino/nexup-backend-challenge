package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepository
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoRepository
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PosesionServiceTest {

    private lateinit var posesionService: PosesionService
    private lateinit var posesionRepository: PosesionRepository
    private lateinit var productoService: ProductoService
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var productoRepository: ProductoRepository
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        posesionRepository = PosesionRepository()
        productoRepository = ProductoRepository()
        supermercadoRepository = SupermercadoRepository()
        productoService = ProductoService(productoRepository)
        supermercadoService = SupermercadoService(supermercadoRepository)
        posesionService = PosesionService(posesionRepository, productoService, supermercadoService)
    }

    @Test
    fun addPosesionTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(1, 1, 50)
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(1, 1)

        assertNotNull(posesion)
        assertEquals(50, posesion?.stock)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(1, 1, 50)

        posesionService.updatePosesionStockByProductoIdSupermercadoId(1, 1, -20)

        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(1, 1)

        assertNotNull(posesion)
        assertEquals(30, posesion?.stock)
    }

    @Test
    fun getStockTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(1, 1, 50)

        val stock = posesionService.getStock(1, 1)

        assertEquals(50, stock)
    }

    @Test
    fun addPosesionInvalidProductoOrSupermercadoTest() {
        val invalidProductoId = 999L
        val invalidSupermercadoId = 999L

        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.addPosesion(invalidProductoId, invalidSupermercadoId, 50)
        }

        assertEquals("Producto o supermercado no encontrado.", exception.message)
    }
}

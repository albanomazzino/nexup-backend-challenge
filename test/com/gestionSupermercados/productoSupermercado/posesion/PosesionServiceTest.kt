package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepository
import com.gestionSupermercados.producto.ProductoRepositoryImpl
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoRepositoryImpl
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.util.*

class PosesionServiceTest {

    private lateinit var posesionService: PosesionService
    private lateinit var posesionRepository: PosesionRepositoryImpl
    private lateinit var productoService: ProductoService
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var productoRepository: ProductoRepository
    private lateinit var supermercadoRepository: SupermercadoRepositoryImpl

    @BeforeEach
    fun setUp() {
        posesionRepository = PosesionRepositoryImpl()
        productoRepository = ProductoRepositoryImpl()
        supermercadoRepository = SupermercadoRepositoryImpl()
        productoService = ProductoService(productoRepository)
        supermercadoService = SupermercadoService(supermercadoRepository)
        posesionService = PosesionService(posesionRepository)
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

        val stock = posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(50, stock)
    }

    @Test
    fun addPosesionTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(50, posesion?.stock)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -20)
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(posesion)
        assertEquals(30, posesion?.stock)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdWithNegativeStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 50)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            posesionService.updatePosesionStockByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1, -51)
        }
        assertEquals("El stock no puede ser menor a 0.", exception.message)
    }
}

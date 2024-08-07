package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstVals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class VentaRepositoryTest {

    private lateinit var ventaRepository: VentaRepository

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepository()
    }

    @Test
    fun addVentaTest() {
        val fechaTest = LocalDateTime.now()
        ventaRepository.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, fechaTest, 10)

        val ventas = ventaRepository.getVentasByProductoIdSupermercadoId(ConstVals.testProducto1, ConstVals.testSupermercado1)

        assertEquals(1, ventas.size)
        assertEquals(ventas[0].productoId, ConstVals.testProducto1)
        assertEquals(ventas[0].supermercadoId, ConstVals.testSupermercado1)
        assertEquals(ventas[0].fecha, fechaTest)
        assertEquals(ventas[0].cantidad, 10)
    }

    @Test
    fun getAllVentasTest() {
        ventaRepository.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaRepository.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 20)
        ventaRepository.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado2, LocalDateTime.now(), 20)

        val ventas = ventaRepository.getAllVentas()

        assertEquals(3, ventas.size)
        assertTrue(ventas[0].productoId == ConstVals.testProducto1)
        assertTrue(ventas[0].supermercadoId == ConstVals.testSupermercado1)
        assertTrue(ventas[0].cantidad == 10)

        assertTrue(ventas[1].productoId == ConstVals.testProducto1)
        assertTrue(ventas[1].supermercadoId == ConstVals.testSupermercado1)
        assertTrue(ventas[1].cantidad == 20)

        assertTrue(ventas[2].productoId == ConstVals.testProducto1)
        assertTrue(ventas[2].supermercadoId == ConstVals.testSupermercado2)
        assertTrue(ventas[2].cantidad == 20)
    }
}
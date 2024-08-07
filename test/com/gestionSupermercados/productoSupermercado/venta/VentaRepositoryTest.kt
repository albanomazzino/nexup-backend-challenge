package com.gestionSupermercados.productoSupermercado.venta

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VentaRepositoryTest {

    private lateinit var ventaRepository: VentaRepository

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepository()
    }

    @Test
    fun addVentaTest() {
        val fechaTest = LocalDateTime.now()
        ventaRepository.addVenta(1, 1, fechaTest, 10)

        val ventas = ventaRepository.getVentasByProductoIdSupermercadoId(1, 1)

        assertEquals(1, ventas.size)
        assertEquals(ventas[0].id, 1)
        assertEquals(ventas[0].productoId, 1)
        assertEquals(ventas[0].supermercadoId, 1)
        assertEquals(ventas[0].fecha, fechaTest)
        assertEquals(ventas[0].cantidad, 10)
    }

    @Test
    fun getAllVentasTest() {
        val venta1 = Venta(1, 1, 1, LocalDateTime.now(), 10)
        val venta2 = Venta(2, 1, 1, LocalDateTime.now(), 20)
        val venta3 = Venta(3, 1, 2, LocalDateTime.now(), 20)

        ventaRepository.addVenta(1, 1, LocalDateTime.now(), 10)
        ventaRepository.addVenta(1, 1, LocalDateTime.now(), 20)
        ventaRepository.addVenta(1, 2, LocalDateTime.now(), 20)

        val ventas = ventaRepository.getAllVentas()

        assertEquals(3, ventas.size)
        assertTrue(ventas[0] == venta1)
        assertTrue(ventas[1] == venta2)
        assertTrue(ventas[2] == venta3)
    }
}
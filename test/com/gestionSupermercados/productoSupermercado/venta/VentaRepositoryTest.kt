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
        val venta = Venta(1, 1, LocalDateTime.now(), 10)
        ventaRepository.addVenta(venta)

        val ventas = ventaRepository.getVentasByProductoId(1)

        assertEquals(1, ventas.size)
        assertEquals(venta, ventas[0])
    }

    @Test
    fun getAllVentasTest() {
        val venta1 = Venta(1, 1, LocalDateTime.now(), 10)
        val venta2 = Venta(2, 1, LocalDateTime.now(), 20)
        ventaRepository.addVenta(venta1)
        ventaRepository.addVenta(venta2)

        val ventas = ventaRepository.getAllVentas()

        assertEquals(2, ventas.size)
        assertTrue(ventas.contains(venta1))
        assertTrue(ventas.contains(venta2))
    }
}
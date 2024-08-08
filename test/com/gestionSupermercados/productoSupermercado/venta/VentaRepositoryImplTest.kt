package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VentaRepositoryImplTest {

    private lateinit var ventaRepository: VentaRepository

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepositoryImpl()
    }

    @Test
    fun addVentaTest() {
        val fechaTest = LocalDateTime.now()
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fechaTest, 10)

        val ventas = ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(1, ventas.size)
        assertEquals(ventas[0].productoId, ConstantValues.testProducto1)
        assertEquals(ventas[0].supermercadoId, ConstantValues.testSupermercado1)
        assertEquals(ventas[0].fecha, fechaTest)
        assertEquals(ventas[0].cantidad, 10)
    }

    @Test
    fun getAllVentasTest() {
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 20)
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 20)

        val ventas = ventaRepository.getAllVentas()

        assertEquals(3, ventas.size)
        assertTrue(ventas[0].productoId == ConstantValues.testProducto1)
        assertTrue(ventas[0].supermercadoId == ConstantValues.testSupermercado1)
        assertTrue(ventas[0].cantidad == 10)

        assertTrue(ventas[1].productoId == ConstantValues.testProducto1)
        assertTrue(ventas[1].supermercadoId == ConstantValues.testSupermercado1)
        assertTrue(ventas[1].cantidad == 20)

        assertTrue(ventas[2].productoId == ConstantValues.testProducto1)
        assertTrue(ventas[2].supermercadoId == ConstantValues.testSupermercado2)
        assertTrue(ventas[2].cantidad == 20)
    }
}
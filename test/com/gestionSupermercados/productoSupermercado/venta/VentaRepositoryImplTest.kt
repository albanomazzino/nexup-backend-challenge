package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class VentaRepositoryImplTest {
    private lateinit var ventaRepository: VentaRepository

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepositoryImpl()
    }

    @Test
    fun addVentaTest() {
        val fechaTest = LocalDateTime.now()
        val cantidadTest = 10

        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fechaTest, cantidadTest)

        val ventas = ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(1, ventas.size)
        assertEquals(ventas[0].productoId, ConstantValues.testProducto1)
        assertEquals(ventas[0].supermercadoId, ConstantValues.testSupermercado1)
        assertEquals(ventas[0].fecha, fechaTest)
        assertEquals(ventas[0].cantidad, cantidadTest)
    }

    @Test
    fun getAllVentasTest() {
        val cantidad1 = 10
        val cantidad2 = 20
        
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidad1)
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidad2)
        ventaRepository.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), cantidad2)

        val ventas = ventaRepository.getAllVentas()

        assertEquals(3, ventas.size)
        assertVenta(ventas[0], ConstantValues.testProducto1, ConstantValues.testSupermercado1, cantidad1)
        assertVenta(ventas[1], ConstantValues.testProducto1, ConstantValues.testSupermercado1, cantidad2)
        assertVenta(ventas[2], ConstantValues.testProducto1, ConstantValues.testSupermercado2, cantidad2)
    }

    private fun assertVenta(venta: Venta, productoId: UUID, supermercadoId: UUID, cantidad: Int) {
        assertEquals(productoId, venta.productoId)
        assertEquals(supermercadoId, venta.supermercadoId)
        assertEquals(cantidad, venta.cantidad)
    }
}
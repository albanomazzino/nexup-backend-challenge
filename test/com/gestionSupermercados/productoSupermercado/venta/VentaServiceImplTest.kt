package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class VentaServiceImplTest {
    private val ventaRepository = mock(VentaRepository::class.java)
    private val productoService = mock(ProductoService::class.java)
    private val posesionService = mock(PosesionService::class.java)
    private lateinit var ventaService: VentaService


    @BeforeEach
    fun setUp() {
        ventaService = VentaServiceImpl(ventaRepository, posesionService, productoService)
    }

    @Test
    fun addVentaTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val cantidadStock = 100
        val cantidadVenta = 10
        val fecha = LocalDateTime.now()

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(cantidadStock)
        `when`(ventaRepository.getAllVentas()).thenReturn(
            listOf(
                Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidadVenta)
            )
        )

        val precioTotalVenta = ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidadVenta)
        val venta = ventaRepository.getAllVentas().lastOrNull()

        assertEquals(cantidadVenta * producto.precio, precioTotalVenta)
        assertNotNull(venta)
        assertVenta(venta!!, ConstantValues.testProducto1, ConstantValues.testSupermercado1, cantidadVenta, fecha)
    }

    @Test
    fun addVentaWithInsufficientStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val cantidadStock = 10
        val cantidadVenta = 20
        val fecha = LocalDateTime.now()

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(cantidadStock)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidadVenta)
        }

        assertEquals("No es posible vender $cantidadVenta unidades, el stock es de $cantidadStock.", exception.message)
        verify(ventaRepository, never()).addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidadVenta)
    }

    @Test
    fun getCantidadVendidaByProductoIdSupermercadoIdTest() {
        val cantidadVenta = 15

        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)
        )

        `when`(ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(ventas)

        val cantidadVendida = ventaService.getCantidadVendidaByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
        assertEquals(cantidadVenta, cantidadVendida)

        verify(ventaRepository).getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
    }

    @Test
    fun getIngresosByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val cantidadVentaA = 10
        val cantidadVentaB = 5

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadVentaA),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadVentaB)
        )

        `when`(ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(ventas)

        val cantidadTotalVendida = cantidadVentaA + cantidadVentaB
        val ingresos = ventaService.getIngresosByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(cantidadTotalVendida * producto.precio, ingresos)
        verify(ventaRepository).getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
    }

    @Test
    fun getIngresosTotalesBySupermercadoIdTest() {
        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        val cantidadVentaProducto1 = 10
        val cantidadVentaProducto2 = 5

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto1)
        `when`(productoService.getProductoById(ConstantValues.testProducto2)).thenReturn(producto2)
        `when`(ventaRepository.getAllVentas()).thenReturn(
            listOf(
                Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadVentaProducto1),
                Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadVentaProducto2)
            )
        )

        val ingresosTotales = ventaService.getIngresosTotalesBySupermercadoId(ConstantValues.testSupermercado1)

        assertEquals(cantidadVentaProducto1 * producto1.precio + cantidadVentaProducto2 * producto2.precio, ingresosTotales)
    }

    private fun assertVenta(venta: Venta, productoId: UUID, supermercadoId: UUID, cantidad: Int, fecha: LocalDateTime) {
        assertEquals(productoId, venta.productoId)
        assertEquals(supermercadoId, venta.supermercadoId)
        assertEquals(cantidad, venta.cantidad)
        assertEquals(fecha, venta.fecha)
    }
}
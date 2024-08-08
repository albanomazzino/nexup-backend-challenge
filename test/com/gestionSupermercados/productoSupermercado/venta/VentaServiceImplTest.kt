package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepository
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class VentaServiceImplTest {
    private lateinit var ventaService: VentaService
    private lateinit var ventaRepository: VentaRepository
    private lateinit var productoService: ProductoService
    private lateinit var posesionService: PosesionService
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var productoRepository: ProductoRepository


    @BeforeEach
    fun setUp() {
        productoRepository = mock(ProductoRepository::class.java)
        ventaRepository = mock(VentaRepository::class.java)
        supermercadoService = mock(SupermercadoService::class.java)
        posesionService = mock(PosesionService::class.java)
        productoService = mock(ProductoService::class.java)
        ventaService = VentaServiceImpl(ventaRepository, posesionService, productoService)
    }

    @Test
    fun addVentaTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado)
        `when`(posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(100)

        val cantidad = 10
        val fecha = LocalDateTime.now()

        `when`(ventaRepository.getAllVentas()).thenReturn(
            listOf(
                Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidad)
            )
        )

        val precioTotalVenta = ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidad)
        assertEquals(cantidad * producto.precio, precioTotalVenta)

        val precioVenta = ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidad)
        assertEquals(precioTotalVenta, precioVenta)

        val ventas = ventaRepository.getAllVentas()
        assertNotNull(ventas.lastOrNull())
        val venta = ventas.lastOrNull()

        assertEquals(ConstantValues.testProducto1, venta?.productoId)
        assertEquals(ConstantValues.testSupermercado1, venta?.supermercadoId)
        assertEquals(cantidad, venta?.cantidad)
        assertEquals(fecha, venta?.fecha)
    }

    @Test
    fun addVentaWithInsufficientStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado)

        val testCantidadStock = 10
        `when`(posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(testCantidadStock)

        val testCantidadVenta = 20
        val testFecha = LocalDateTime.now()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, testFecha, testCantidadVenta)
        }

        assertEquals("No es posible vender $testCantidadVenta unidades, el stock es de $testCantidadStock.", exception.message)
        verify(ventaRepository, times(0)).addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, testFecha, testCantidadVenta)
    }

    @Test
    fun getCantidadVendidaByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado)

        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)
        )

        `when`(ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(ventas)

        val cantidadVendida = ventaService.getCantidadVendidaByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
        assertEquals(15, cantidadVendida)

        verify(ventaRepository).getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
    }

    @Test
    fun getIngresosByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto)
        `when`(supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado)

        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)
        )

        `when`(ventaRepository.getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)).thenReturn(ventas)

        val ingresos = ventaService.getIngresosByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
        assertEquals(15 * producto.precio, ingresos)

        verify(ventaRepository).getVentasByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)
    }

    @Test
    fun getIngresosTotalesBySupermercadoIdTest() {
        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        val cantidadProducto1 = 10
        val cantidadProducto2 = 5

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto1)
        `when`(productoService.getProductoById(ConstantValues.testProducto2)).thenReturn(producto2)
        `when`(ventaRepository.getAllVentas()).thenReturn(
            listOf(
                Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadProducto1),
                Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadProducto2)
            )
        )

        val ingresosTotales = ventaService.getIngresosTotalesBySupermercadoId(ConstantValues.testSupermercado1)

        assertEquals(cantidadProducto1 * producto1.precio + cantidadProducto2 * producto2.precio, ingresosTotales)
    }

}
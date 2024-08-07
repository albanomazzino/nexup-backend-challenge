package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepository
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionRepository
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoRepository
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VentaServiceTest {

    private lateinit var ventaService: VentaService
    private lateinit var ventaRepository: VentaRepository
    private lateinit var productoService: ProductoService
    private lateinit var posesionService: PosesionService
    private lateinit var productoRepository: ProductoRepository
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var posesionRepository: PosesionRepository
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepository()
        productoRepository = ProductoRepository()
        supermercadoRepository = SupermercadoRepository()
        posesionRepository = PosesionRepository()
        productoService = ProductoService(productoRepository)
        supermercadoService = SupermercadoService(supermercadoRepository)
        posesionService = PosesionService(posesionRepository, productoService, supermercadoService)
        ventaService = VentaService(ventaRepository, posesionService, productoService)
    }

    @Test
    fun addVentaTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(1, 1, 100)

        val cantidad = 10
        val fecha = LocalDateTime.now()
        val precioTotalVenta = ventaService.addVenta(1, 1, fecha, cantidad)

        assertEquals(cantidad * producto.precio, precioTotalVenta)

        val venta = ventaRepository.getAllVentas().lastOrNull()
        assertNotNull(venta)
        assertEquals(1, venta?.id)
        assertEquals(1, venta?.productoId)
        assertEquals(1, venta?.supermercadoId)
        assertEquals(cantidad, venta?.cantidad)
        assertEquals(fecha, venta?.fecha)
        assertEquals(100 - cantidad, posesionService.getStock(1, 1))

        // Otra venta consecutiva
        val fechaVenta2 = LocalDateTime.now()
        ventaService.addVenta(1, 1, fechaVenta2, cantidad)

        val venta2 = ventaRepository.getAllVentas().lastOrNull()
        assertNotNull(venta2)
        assertEquals(2, venta2?.id)
        assertEquals(1, venta2?.productoId)
        assertEquals(1, venta2?.supermercadoId)
        assertEquals(cantidad, venta2?.cantidad)
        assertEquals(fechaVenta2, venta2?.fecha)
        assertEquals(100 - 2 * cantidad, posesionService.getStock(1, 1))


    }

    @Test
    fun getCantidadVendidaByProductoIdSupermercadoIdTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(1, 1, 100)

        ventaService.addVenta(1, 1, LocalDateTime.now(), 10)
        ventaService.addVenta(1, 1, LocalDateTime.now(), 5)

        val cantidadVendida = ventaService.getCantidadVendidaByProductoIdSupermercadoId(1, 1)

        assertEquals(15, cantidadVendida)
    }

    @Test
    fun getIngresosByProductoIdSupermercadoIdTest() {
        val producto = Producto(1, "Carne", 10.0)
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(1, 1, 100)

        ventaService.addVenta(1, 1, LocalDateTime.now(), 10)
        ventaService.addVenta(1, 1, LocalDateTime.now(), 5)

        val ingresos = ventaService.getIngresosByProductoIdSupermercadoId(1, 1)
        assertEquals(15 * producto.precio, ingresos)
    }

    @Test
    fun getIngresosTotalesBySupermercadoIdTest() {
        val producto1 = Producto(1, "Carne", 10.0)
        val producto2 = Producto(2, "Pescado", 20.0)
        val cantidadProducto1 = 10
        val cantidadProducto2 = 5
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto1)
        productoService.addProducto(producto2)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(1, 1, 100)
        posesionService.addPosesion(2, 1, 100)

        ventaService.addVenta(1, 1, LocalDateTime.now(), cantidadProducto1)
        ventaService.addVenta(2, 1, LocalDateTime.now(), cantidadProducto2)

        val ingresosTotales = ventaService.getIngresosTotalesBySupermercadoId(1)

        assertEquals(cantidadProducto1 * producto1.precio + cantidadProducto2 * producto2.precio, ingresosTotales)
    }
}
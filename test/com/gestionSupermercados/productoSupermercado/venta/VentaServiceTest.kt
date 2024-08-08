package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepositoryImpl
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionRepositoryImpl
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoRepositoryImpl
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VentaServiceTest {

    private lateinit var ventaService: VentaService
    private lateinit var ventaRepository: VentaRepositoryImpl
    private lateinit var productoService: ProductoService
    private lateinit var posesionService: PosesionService
    private lateinit var productoRepository: ProductoRepositoryImpl
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var posesionRepository: PosesionRepositoryImpl
    private lateinit var supermercadoRepository: SupermercadoRepositoryImpl

    @BeforeEach
    fun setUp() {
        ventaRepository = VentaRepositoryImpl()
        productoRepository = ProductoRepositoryImpl()
        supermercadoRepository = SupermercadoRepositoryImpl()
        posesionRepository = PosesionRepositoryImpl()
        productoService = ProductoService(productoRepository)
        supermercadoService = SupermercadoService(supermercadoRepository)
        posesionService = PosesionService(posesionRepository)
        ventaService = VentaService(ventaRepository, posesionService, productoService)
    }

    @Test
    fun addVentaTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 100)

        val cantidad = 10
        val fecha = LocalDateTime.now()
        val precioTotalVenta = ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fecha, cantidad)

        assertEquals(cantidad * producto.precio, precioTotalVenta)

        val venta = ventaRepository.getAllVentas().lastOrNull()
        assertNotNull(venta)
        assertEquals(ConstantValues.testProducto1, venta?.productoId)
        assertEquals(ConstantValues.testSupermercado1, venta?.supermercadoId)
        assertEquals(cantidad, venta?.cantidad)
        assertEquals(fecha, venta?.fecha)
        assertEquals(100 - cantidad, posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1))

        val fechaVenta2 = LocalDateTime.now()
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, fechaVenta2, cantidad)

        val venta2 = ventaRepository.getAllVentas().lastOrNull()
        assertNotNull(venta2)
        assertEquals(ConstantValues.testProducto1, venta2?.productoId)
        assertEquals(ConstantValues.testSupermercado1, venta2?.supermercadoId)
        assertEquals(cantidad, venta2?.cantidad)
        assertEquals(fechaVenta2, venta2?.fecha)
        assertEquals(100 - 2 * cantidad, posesionService.getStock(ConstantValues.testProducto1, ConstantValues.testSupermercado1))
    }

    @Test
    fun addVentaWithInsufficientStockTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)

        val testCantidadStock = 10
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, testCantidadStock)

        val testCantidadVenta = 20
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), testCantidadVenta)
        }

        assertEquals("No es posible vender $testCantidadVenta unidades, el stock es de $testCantidadStock.", exception.message)
    }

    @Test
    fun getCantidadVendidaByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 100)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)

        val cantidadVendida = ventaService.getCantidadVendidaByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(15, cantidadVendida)
    }

    @Test
    fun getIngresosByProductoIdSupermercadoIdTest() {
        val producto = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 100)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)

        val ingresos = ventaService.getIngresosByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertEquals(15 * producto.precio, ingresos)
    }

    @Test
    fun getIngresosTotalesBySupermercadoIdTest() {
        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        val cantidadProducto1 = 10
        val cantidadProducto2 = 5
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        productoService.addProducto(producto1)
        productoService.addProducto(producto2)
        supermercadoService.addSupermercado(supermercado)
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 100)
        posesionService.addPosesion(ConstantValues.testProducto2, ConstantValues.testSupermercado1, 100)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadProducto1)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), cantidadProducto2)

        val ingresosTotales = ventaService.getIngresosTotalesBySupermercadoId(ConstantValues.testSupermercado1)

        assertEquals(cantidadProducto1 * producto1.precio + cantidadProducto2 * producto2.precio, ingresosTotales)
    }
}
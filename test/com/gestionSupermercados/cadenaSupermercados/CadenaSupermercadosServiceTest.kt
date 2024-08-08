package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoRepository
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionRepository
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.productoSupermercado.venta.VentaRepository
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoRepository
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CadenaSupermercadosServiceTest {

    private lateinit var cadenaSupermercadosService: CadenaSupermercadosService
    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepository
    private lateinit var ventaService: VentaService
    private lateinit var productoService: ProductoService
    private lateinit var productoRepository: ProductoRepository
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var supermercadoRepository: SupermercadoRepository
    private lateinit var posesionRepository: PosesionRepository
    private lateinit var posesionService: PosesionService
    private lateinit var ventaRepository: VentaRepository
    private lateinit var cadenaSupermercados: CadenaSupermercados

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = CadenaSupermercadosRepository()
        productoRepository = ProductoRepository()
        supermercadoRepository = SupermercadoRepository()
        ventaRepository = VentaRepository()
        posesionRepository = PosesionRepository()
        productoService = ProductoService(productoRepository)
        supermercadoService = SupermercadoService(supermercadoRepository)
        posesionService = PosesionService(posesionRepository)
        ventaService = VentaService(ventaRepository, posesionService, productoService)
        cadenaSupermercadosService = CadenaSupermercadosService(cadenaSupermercadosRepository, ventaService, productoService)

        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        val producto3 = Producto(ConstantValues.testProducto3, "Pollo", 30.0)
        val producto4 = Producto(ConstantValues.testProducto4, "Cerdo", 45.0)
        val producto5 = Producto(ConstantValues.testProducto5, "Ternera", 50.0)
        val producto6 = Producto(ConstantValues.testProducto6, "Cordero", 65.0)

        productoService.addProducto(producto1)
        productoService.addProducto(producto2)
        productoService.addProducto(producto3)
        productoService.addProducto(producto4)
        productoService.addProducto(producto5)
        productoService.addProducto(producto6)

        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A1", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado A2", 8, 20, listOf("Lunes", "Martes"))
        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        val supermercadosParaLaCadena = mutableListOf<Supermercado>()
        supermercadosParaLaCadena.add(supermercado1)
        supermercadosParaLaCadena.add(supermercado2)

        cadenaSupermercados = CadenaSupermercados(ConstantValues.testCadena1, "Cadena A", supermercadosParaLaCadena)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadenaSupermercados)

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 20) // 20 unidades de Carne en Supermercado A1
        posesionService.addPosesion(ConstantValues.testProducto2, ConstantValues.testSupermercado1, 10) // 10 unidades de Pescado en Supermercado A1
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado2, 15) // 15 unidades de Carne en Supermercado A2
        posesionService.addPosesion(ConstantValues.testProducto3, ConstantValues.testSupermercado2, 25) // 25 unidades de Pollo en Supermercado A2
    }

    @Test
    fun getCincoProductosMasVendidosTest() {
        posesionService.addPosesion(ConstantValues.testProducto3, ConstantValues.testSupermercado1, 15)
        posesionService.addPosesion(ConstantValues.testProducto4, ConstantValues.testSupermercado1, 7)
        posesionService.addPosesion(ConstantValues.testProducto5, ConstantValues.testSupermercado1, 12)
        posesionService.addPosesion(ConstantValues.testProducto5, ConstantValues.testSupermercado2, 4)
        posesionService.addPosesion(ConstantValues.testProducto6, ConstantValues.testSupermercado2, 5)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)
        ventaService.addVenta(ConstantValues.testProducto3, ConstantValues.testSupermercado1, LocalDateTime.now(), 15)
        ventaService.addVenta(ConstantValues.testProducto4, ConstantValues.testSupermercado1, LocalDateTime.now(), 7)
        ventaService.addVenta(ConstantValues.testProducto5, ConstantValues.testSupermercado1, LocalDateTime.now(), 12)
        ventaService.addVenta(ConstantValues.testProducto5, ConstantValues.testSupermercado2, LocalDateTime.now(), 4)
        ventaService.addVenta(ConstantValues.testProducto6, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestConMenosDe5Productos() {

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 9)

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Carne: 10-Pescado: 9", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestSinVentas() {
        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("No hay ventas registradas para la cadena de supermercados ${cadenaSupermercados.id}.", top5MasVendidos)
    }

    @Test
    fun getIngresosTotalesSupermercadosConVentasTest() {
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 5)
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 7)
        ventaService.addVenta(ConstantValues.testProducto3, ConstantValues.testSupermercado2, LocalDateTime.now(), 12)

        val resultado = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstantValues.testCadena1)

        assertEquals(10.0 * 10 + 20.0 * 5 + 30.0 * 12 + 10.0 * 7, resultado)
    }

    @Test
    fun getIngresosTotalesSupermercadosSinVentasTest() {
        val resultado = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstantValues.testCadena1)

        assertEquals(0.0, resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTest() {
        posesionService.addPosesion(ConstantValues.testProducto2, ConstantValues.testSupermercado2, 10)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 15)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("Supermercado A2 (${ConstantValues.testSupermercado2}). Ingresos totales: 250.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosEmpateTest() {
        posesionService.addPosesion(ConstantValues.testProducto2, ConstantValues.testSupermercado2, 10)

        ventaService.addVenta(ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("Supermercado A1 (${ConstantValues.testSupermercado1}). Ingresos totales: 100.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTestSinVentas() {
        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("No hay ventas registradas para la cadena de supermercados ${ConstantValues.testCadena1}.", resultado)
    }
}

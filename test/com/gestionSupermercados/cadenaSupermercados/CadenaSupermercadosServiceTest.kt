package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstVals
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
        posesionService = PosesionService(posesionRepository, productoService, supermercadoService)
        ventaService = VentaService(ventaRepository, posesionService, productoService)
        cadenaSupermercadosService = CadenaSupermercadosService(cadenaSupermercadosRepository, ventaService, productoService)


        val producto1 = Producto(ConstVals.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstVals.testProducto2, "Pescado", 20.0)
        val producto3 = Producto(ConstVals.testProducto3, "Pollo", 30.0)
        val producto4 = Producto(ConstVals.testProducto4, "Cerdo", 45.0)
        val producto5 = Producto(ConstVals.testProducto5, "Ternera", 50.0)
        val producto6 = Producto(ConstVals.testProducto6, "Cordero", 65.0)

        productoService.addProducto(producto1)
        productoService.addProducto(producto2)
        productoService.addProducto(producto3)
        productoService.addProducto(producto4)
        productoService.addProducto(producto5)
        productoService.addProducto(producto6)

        // Crear una cadena de supermercados y añadir algunos supermercados
        val supermercado1 = Supermercado(ConstVals.testSupermercado1, "Supermercado A1", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstVals.testSupermercado2, "Supermercado A2", 8, 20, listOf("Lunes", "Martes"))
        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        val supermercadosParaLaCadena = mutableListOf<Supermercado>()
        supermercadosParaLaCadena.add(supermercado1)
        supermercadosParaLaCadena.add(supermercado2)

        cadenaSupermercados = CadenaSupermercados(ConstVals.testCadena1, "Cadena A", supermercadosParaLaCadena)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadenaSupermercados)

        posesionService.addPosesion(ConstVals.testProducto1, ConstVals.testSupermercado1, 20) // 20 unidades de Carne en Supermercado A1
        posesionService.addPosesion(ConstVals.testProducto2, ConstVals.testSupermercado1, 10) // 10 unidades de Pescado en Supermercado A1
        posesionService.addPosesion(ConstVals.testProducto1, ConstVals.testSupermercado2, 15) // 15 unidades de Carne en Supermercado A2
        posesionService.addPosesion(ConstVals.testProducto3, ConstVals.testSupermercado2, 25) // 25 unidades de Pollo en Supermercado A2
    }

    @Test
    fun getCincoProductosMasVendidosTest() {
        // Agregar stock y ventas para los 5 productos
        posesionService.addPosesion(ConstVals.testProducto3, ConstVals.testSupermercado1, 15)
        posesionService.addPosesion(ConstVals.testProducto4, ConstVals.testSupermercado1, 7)
        posesionService.addPosesion(ConstVals.testProducto5, ConstVals.testSupermercado1, 12)
        posesionService.addPosesion(ConstVals.testProducto5, ConstVals.testSupermercado2, 4)
        posesionService.addPosesion(ConstVals.testProducto6, ConstVals.testSupermercado2, 5)

        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstVals.testProducto2, ConstVals.testSupermercado1, LocalDateTime.now(), 5)
        ventaService.addVenta(ConstVals.testProducto3, ConstVals.testSupermercado1, LocalDateTime.now(), 15)
        ventaService.addVenta(ConstVals.testProducto4, ConstVals.testSupermercado1, LocalDateTime.now(), 7)
        ventaService.addVenta(ConstVals.testProducto5, ConstVals.testSupermercado1, LocalDateTime.now(), 12)
        ventaService.addVenta(ConstVals.testProducto5, ConstVals.testSupermercado2, LocalDateTime.now(), 4)
        ventaService.addVenta(ConstVals.testProducto6, ConstVals.testSupermercado2, LocalDateTime.now(), 5)

        // Añadir ventas
        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstVals.testCadena1)
        assertEquals("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestConMenosDe5Productos() {
        // Agregar stock y ventas para 2 productos
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstVals.testProducto2, ConstVals.testSupermercado1, LocalDateTime.now(), 9)

        // Añadir ventas
        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstVals.testCadena1)
        assertEquals("Carne: 10-Pescado: 9", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestSinVentas() {
        // No se agregan ventas

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstVals.testCadena1)

        assertEquals("No hay ventas registradas para la cadena de supermercados ${cadenaSupermercados.id}.", top5MasVendidos)
    }

    @Test
    fun getIngresosTotalesSupermercadosConVentasTest() {
        // Agregar ventas en ambos supermercados
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstVals.testProducto2, ConstVals.testSupermercado1, LocalDateTime.now(), 5)
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado2, LocalDateTime.now(), 7)
        ventaService.addVenta(ConstVals.testProducto3, ConstVals.testSupermercado2, LocalDateTime.now(), 12)

        // Obtener ingresos totales para la cadena de supermercados
        val resultado = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstVals.testCadena1)
        assertEquals(10.0 * 10 + 20.0 * 5 + 30.0 * 12 + 10.0 * 7, resultado)
    }

    @Test
    fun getIngresosTotalesSupermercadosSinVentasTest() {
        // No se agregan ventas

        // Obtener ingresos totales para la cadena de supermercados
        val resultado = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstVals.testCadena1)
        assertEquals(0.0, resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTest() {
        posesionService.addPosesion(ConstVals.testProducto2, ConstVals.testSupermercado2, 10)

        // Agregar stock y ventas
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado2, LocalDateTime.now(), 15)
        ventaService.addVenta(ConstVals.testProducto2, ConstVals.testSupermercado2, LocalDateTime.now(), 5)

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstVals.testCadena1)
        assertEquals("Supermercado A2 (${ConstVals.testSupermercado2}). Ingresos totales: 250.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosEmpateTest() {
        posesionService.addPosesion(ConstVals.testProducto2, ConstVals.testSupermercado2, 10)

        // Agregar stock y ventas
        ventaService.addVenta(ConstVals.testProducto1, ConstVals.testSupermercado1, LocalDateTime.now(), 10)
        ventaService.addVenta(ConstVals.testProducto2, ConstVals.testSupermercado2, LocalDateTime.now(), 5)

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstVals.testCadena1)
        assertEquals("Supermercado A1 (${ConstVals.testSupermercado1}). Ingresos totales: 100.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTestSinVentas() {
        // No se agregan ventas

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstVals.testCadena1)
        assertEquals("No hay ventas registradas para la cadena de supermercados ${ConstVals.testCadena1}.", resultado)
    }
}

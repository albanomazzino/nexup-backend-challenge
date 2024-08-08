package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.productoSupermercado.venta.Venta
import com.gestionSupermercados.productoSupermercado.venta.VentaRepository
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import com.gestionSupermercados.supermercado.Supermercado
import com.gestionSupermercados.supermercado.SupermercadoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.UUID


class CadenaSupermercadosServiceImplTest {
    private lateinit var cadenaSupermercadosService: CadenaSupermercadosService
    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepository
    private lateinit var ventaService: VentaService
    private lateinit var productoService: ProductoService
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var posesionService: PosesionService
    private lateinit var ventaRepository: VentaRepository
    private lateinit var cadenaSupermercadosOutputFormatter: CadenaSupermercadosOutputFormatter

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = mock(CadenaSupermercadosRepository::class.java)
        ventaRepository = mock(VentaRepository::class.java)
        productoService = mock(ProductoService::class.java)
        supermercadoService = mock(SupermercadoService::class.java)
        posesionService = mock(PosesionService::class.java)
        cadenaSupermercadosOutputFormatter = mock(CadenaSupermercadosOutputFormatter::class.java)
        ventaService = mock(VentaService::class.java)

        cadenaSupermercadosService = CadenaSupermercadosServiceImpl(cadenaSupermercadosRepository, ventaService, productoService, cadenaSupermercadosOutputFormatter)

        val producto1 = Producto(ConstantValues.testProducto1, "Carne", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Pescado", 20.0)
        val producto3 = Producto(ConstantValues.testProducto3, "Pollo", 30.0)
        val producto4 = Producto(ConstantValues.testProducto4, "Cerdo", 45.0)
        val producto5 = Producto(ConstantValues.testProducto5, "Ternera", 50.0)
        val producto6 = Producto(ConstantValues.testProducto6, "Cordero", 65.0)

        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A1", 9, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado A2", 8, 20, listOf("Lunes", "Martes"))

        `when`(cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)).thenReturn(listOf(supermercado1, supermercado2))
        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto1)
        `when`(productoService.getProductoById(ConstantValues.testProducto2)).thenReturn(producto2)
        `when`(productoService.getProductoById(ConstantValues.testProducto3)).thenReturn(producto3)
        `when`(productoService.getProductoById(ConstantValues.testProducto4)).thenReturn(producto4)
        `when`(productoService.getProductoById(ConstantValues.testProducto5)).thenReturn(producto5)
        `when`(productoService.getProductoById(ConstantValues.testProducto6)).thenReturn(producto6)
        `when`(ventaService.getAllVentas()).thenReturn(emptyList()) // Default to empty list for initial setup

        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 20) // 20 unidades de Carne en Supermercado A1
        posesionService.addPosesion(ConstantValues.testProducto2, ConstantValues.testSupermercado1, 10) // 10 unidades de Pescado en Supermercado A1
        posesionService.addPosesion(ConstantValues.testProducto1, ConstantValues.testSupermercado2, 15) // 15 unidades de Carne en Supermercado A2
        posesionService.addPosesion(ConstantValues.testProducto3, ConstantValues.testSupermercado2, 25) // 25 unidades de Pollo en Supermercado A2
    }

    @Test
    fun getCincoProductosMasVendidosTest() {
        val testFecha = LocalDateTime.now()
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, testFecha, 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, testFecha, 5),
            Venta(UUID.randomUUID(), ConstantValues.testProducto3, ConstantValues.testSupermercado1, testFecha, 15),
            Venta(UUID.randomUUID(), ConstantValues.testProducto4, ConstantValues.testSupermercado1, testFecha, 7),
            Venta(UUID.randomUUID(), ConstantValues.testProducto5, ConstantValues.testSupermercado1, testFecha, 12),
            Venta(UUID.randomUUID(), ConstantValues.testProducto5, ConstantValues.testSupermercado2, testFecha, 4),
            Venta(UUID.randomUUID(), ConstantValues.testProducto6, ConstantValues.testSupermercado2, testFecha, 5)
        )
        `when`(ventaService.getAllVentas()).thenReturn(ventas)
        `when`(cadenaSupermercadosOutputFormatter.formatTopProductos(anyList())).thenReturn("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5")

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestConMenosDe5Productos() {
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 9)
        )
        `when`(ventaService.getAllVentas()).thenReturn(ventas)
        `when`(cadenaSupermercadosOutputFormatter.formatTopProductos(anyList())).thenReturn("Carne: 10-Pescado: 9")

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Carne: 10-Pescado: 9", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestSinVentas() {
        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)
        assertEquals("No hay ventas registradas para la cadena de supermercados ${ConstantValues.testCadena1}.", top5MasVendidos)
    }

    @Test
    fun getIngresosTotalesSupermercadosConVentasTest() {
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 5),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 7),
            Venta(UUID.randomUUID(), ConstantValues.testProducto3, ConstantValues.testSupermercado2, LocalDateTime.now(), 12)
        )

        `when`(ventaService.getAllVentas()).thenReturn(ventas)

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
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 15),
            Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)
        )

        `when`(ventaService.getAllVentas()).thenReturn(ventas)

        val supermercadosCadena : List<Supermercado> = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)!! // non null-asserted respaldado por mock en setUp()
        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadoConMasIngresos(ConstantValues.testSupermercado2,250.0, supermercadosCadena))
            .thenReturn("Supermercado A2 (${ConstantValues.testSupermercado2}). Ingresos totales: 250.0")

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("Supermercado A2 (${ConstantValues.testSupermercado2}). Ingresos totales: 250.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosEmpateTest() {
        val ventas = listOf(
            Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
            Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)
        )

        `when`(ventaService.getAllVentas()).thenReturn(ventas)

        val supermercadosCadena : List<Supermercado> = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)!! // non null-asserted respaldado por mock en setUp()
        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadoConMasIngresos(ConstantValues.testSupermercado1,100.0, supermercadosCadena))
            .thenReturn("Supermercado A1 (${ConstantValues.testSupermercado1}). Ingresos totales: 100.0")

        val resultado = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("Supermercado A1 (${ConstantValues.testSupermercado1}). Ingresos totales: 100.0", resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTestSinVentas() {
        val supermercadoMayoresIngresosEnCadenaSupermercados = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)
        assertEquals("No hay ventas registradas para la cadena de supermercados ${ConstantValues.testCadena1}.", supermercadoMayoresIngresosEnCadenaSupermercados)
    }

    @Test
    fun getAllSupermercadosAbiertosUnResultadoTest() {
        val testSupermercadosAbiertos = listOf(Supermercado(ConstantValues.testSupermercado2, "Supermercado A2", 8, 20, listOf("Lunes", "Martes")))

        `when`(cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstantValues.testCadena1, 8, "Lunes"))
            .thenReturn(testSupermercadosAbiertos)

        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadosAbiertos(testSupermercadosAbiertos))
            .thenReturn("Supermercado A2 (${ConstantValues.testSupermercado2})")

        val supermercadosAbiertos = cadenaSupermercadosService.getAllSupermercadosAbiertos(
            ConstantValues.testCadena1,
            hora = 8,
            dia = "Lunes"
        )

        assertEquals("Supermercado A2 (${ConstantValues.testSupermercado2})", supermercadosAbiertos)
    }

    @Test
    fun getAllSupermercadosAbiertosVariosResultadosTest() {
        val testSupermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)

        `when`(cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstantValues.testCadena1, 9, "Lunes"))
            .thenReturn(testSupermercadosAbiertos)

        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadosAbiertos(testSupermercadosAbiertos!!)) // non-null operator backed by setUp method
            .thenReturn("Supermercado A1 (${ConstantValues.testSupermercado1}), Supermercado A2 (${ConstantValues.testSupermercado2})")

        val supermercadosAbiertos = cadenaSupermercadosService.getAllSupermercadosAbiertos(
            ConstantValues.testCadena1,
            hora = 9,
            dia = "Lunes"
        )

        assertEquals("Supermercado A1 (${ConstantValues.testSupermercado1}), Supermercado A2 (${ConstantValues.testSupermercado2})", supermercadosAbiertos)
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesCase1Test() {
        val supermercadosAbiertos = cadenaSupermercadosService.getAllSupermercadosAbiertos(
            ConstantValues.testCadena1,
            hora = 8,
            dia = "Miércoles"
        )

        assertEquals("No se encontraron supermercados abiertos.", supermercadosAbiertos)
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesCase2Test() {
        val supermercadosAbiertos = cadenaSupermercadosService.getAllSupermercadosAbiertos(
            ConstantValues.testCadena1,
            hora = 21,
            dia = "Lunes"
        )

        assertEquals("No se encontraron supermercados abiertos.", supermercadosAbiertos)
    }
}

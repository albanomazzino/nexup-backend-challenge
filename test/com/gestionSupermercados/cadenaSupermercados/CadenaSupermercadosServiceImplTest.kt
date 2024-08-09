package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.venta.Venta
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.UUID

class CadenaSupermercadosServiceImplTest {
    private val cadenaSupermercadosRepository: CadenaSupermercadosRepository = mock(CadenaSupermercadosRepository::class.java)
    private val productoService: ProductoService = mock(ProductoService::class.java)
    private val cadenaSupermercadosOutputFormatter: CadenaSupermercadosOutputFormatter = mock(CadenaSupermercadosOutputFormatter::class.java)
    private val ventaService: VentaService = mock(VentaService::class.java)
    private lateinit var cadenaSupermercadosService : CadenaSupermercadosService

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosService = CadenaSupermercadosServiceImpl(cadenaSupermercadosRepository, ventaService, productoService, cadenaSupermercadosOutputFormatter)

        val productos = crearProductos()
        val supermercados = crearSupermercados()


        `when`(cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)).thenReturn(supermercados)

        productos.forEach { producto ->
            `when`(productoService.getProductoById(producto.id)).thenReturn(producto)
        }
    }

    @Test
    fun addCadenaSupermercadosTest() {
        val supermercados = crearSupermercados().toMutableList()
        val cadenaSupermercados = CadenaSupermercados(ConstantValues.testCadena1, "Cadena A", supermercados)

        doNothing().`when`(cadenaSupermercadosRepository).addCadenaSupermercados(cadenaSupermercados)

        cadenaSupermercadosService.addCadenaSupermercados(cadenaSupermercados)

        verify(cadenaSupermercadosRepository).addCadenaSupermercados(cadenaSupermercados)
        verifyNoMoreInteractions(cadenaSupermercadosRepository)
    }

    @Test
    fun addCadenaSupermercadosNombreBlankTest() {
        val supermercados = crearSupermercados().toMutableList()
        val cadenaSupermercados = CadenaSupermercados(ConstantValues.testCadena1, "   ", supermercados)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            cadenaSupermercadosService.addCadenaSupermercados(cadenaSupermercados)
        }

        assertEquals("El nombre de la cadena de supermercados no puede estar en blanco.", exception.message)
        verifyNoInteractions(cadenaSupermercadosRepository)
    }

    @Test
    fun addCadenaSupermercadosListaSupermercadosVaciaTest() {
        val cadenaSupermercados = CadenaSupermercados(ConstantValues.testCadena1, "Cadena A", mutableListOf<Supermercado>())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            cadenaSupermercadosService.addCadenaSupermercados(cadenaSupermercados)
        }

        assertEquals("La lista de supermercados no puede estar vacía o ser nula.", exception.message)
        verifyNoInteractions(cadenaSupermercadosRepository)
    }


    @Test
    fun getCincoProductosMasVendidosTest() {
        `when`(ventaService.getAllVentas()).thenReturn(crearVentasConMasDeCincoProductos())
        `when`(cadenaSupermercadosOutputFormatter.formatTopProductos(anyList())).thenReturn("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5")

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Ternera: 16-Pollo: 15-Carne: 10-Cerdo: 7-Pescado: 5", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestConMenosDe5Productos() {
        `when`(ventaService.getAllVentas()).thenReturn(crearVentasConMenosDeCincoProductos())
        `when`(cadenaSupermercadosOutputFormatter.formatTopProductos(anyList())).thenReturn("Carne: 10-Pescado: 9")

        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals("Carne: 10-Pescado: 9", top5MasVendidos)
    }

    @Test
    fun getCincoProductosMasVendidosTestSinVentas() {
        val top5MasVendidos = cadenaSupermercadosService.getCincoProductosMasVendidos(ConstantValues.testCadena1)

        assertEquals(noVentasMessage(ConstantValues.testCadena1), top5MasVendidos)
    }

    @Test
    fun getIngresosTotalesSupermercadosConVentasTest() {
        `when`(ventaService.getAllVentas()).thenReturn(crearVentasParaIngresosCadena())

        val ingresosTotalesCadena = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstantValues.testCadena1)

        assertEquals(10.0 * 10 + 20.0 * 5 + 30.0 * 12 + 10.0 * 7, ingresosTotalesCadena)
    }

    @Test
    fun getIngresosTotalesSupermercadosSinVentasTest() {
        val resultado = cadenaSupermercadosService.getIngresosTotalesSupermercados(ConstantValues.testCadena1)

        assertEquals(0.0, resultado)
    }

    @Test
    fun getSupermercadoMayoresIngresosTest() {
        `when`(ventaService.getAllVentas()).thenReturn(crearVentasParaSupermercadoMayoresIngresos())

        val supermercadosCadena : List<Supermercado> = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)!! // non null-asserted respaldado por mock en setUp()
        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadoConMasIngresos(ConstantValues.testSupermercado2,250.0, supermercadosCadena))
            .thenReturn("Supermercado A2 (${ConstantValues.testSupermercado2}). Ingresos totales: 250.0")

        val supermercadoMayoresIngresos = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)

        assertEquals(supermercadoIngresosTotalesMessage("Supermercado A2", ConstantValues.testSupermercado2, 250.0), supermercadoMayoresIngresos)
    }

    @Test
    fun getSupermercadoMayoresIngresosEmpateTest() {
        `when`(ventaService.getAllVentas()).thenReturn(crearVentasParaSupermercadoMayoresIngresosEmpate())

        val supermercadosCadena : List<Supermercado> = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)!! // non null-asserted respaldado por mock en setUp()

        `when`(cadenaSupermercadosOutputFormatter.formatSupermercadoConMasIngresos(ConstantValues.testSupermercado1,100.0, supermercadosCadena))
            .thenReturn("Supermercado A1 (${ConstantValues.testSupermercado1}). Ingresos totales: 100.0")

        val supermercadoMayoresIngresos = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)


        assertEquals(supermercadoIngresosTotalesMessage("Supermercado A1", ConstantValues.testSupermercado1, 100.0), supermercadoMayoresIngresos)
    }

    @Test
    fun getSupermercadoMayoresIngresosTestSinVentas() {
        val supermercadoMayoresIngresos = cadenaSupermercadosService.getSupermercadoMayoresIngresos(ConstantValues.testCadena1)

        assertEquals(noVentasMessage(ConstantValues.testCadena1), supermercadoMayoresIngresos)
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

        assertEquals(ConstantValues.NO_SUPERMERCADOS_ABIERTOS_MESSAGE, supermercadosAbiertos)
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesCase2Test() {
        val supermercadosAbiertos = cadenaSupermercadosService.getAllSupermercadosAbiertos(
            ConstantValues.testCadena1,
            hora = 21,
            dia = "Lunes"
        )

        assertEquals(ConstantValues.NO_SUPERMERCADOS_ABIERTOS_MESSAGE, supermercadosAbiertos)
    }

    private fun crearProductos() = listOf(
        Producto(ConstantValues.testProducto1, "Carne", 10.0),
        Producto(ConstantValues.testProducto2, "Pescado", 20.0),
        Producto(ConstantValues.testProducto3, "Pollo", 30.0),
        Producto(ConstantValues.testProducto4, "Cerdo", 45.0),
        Producto(ConstantValues.testProducto5, "Ternera", 50.0),
        Producto(ConstantValues.testProducto6, "Cordero", 65.0)
    )

    private fun crearSupermercados() = listOf(
        Supermercado(ConstantValues.testSupermercado1, "Supermercado A1", 9, 20, listOf("Lunes", "Martes")),
        Supermercado(ConstantValues.testSupermercado2, "Supermercado A2", 8, 20, listOf("Lunes", "Martes"))
    )

    private fun crearVentasConMasDeCincoProductos() = listOf(
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
        Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 5),
        Venta(UUID.randomUUID(), ConstantValues.testProducto3, ConstantValues.testSupermercado1, LocalDateTime.now(), 15),
        Venta(UUID.randomUUID(), ConstantValues.testProducto4, ConstantValues.testSupermercado1, LocalDateTime.now(), 7),
        Venta(UUID.randomUUID(), ConstantValues.testProducto5, ConstantValues.testSupermercado1, LocalDateTime.now(), 12),
        Venta(UUID.randomUUID(), ConstantValues.testProducto5, ConstantValues.testSupermercado2, LocalDateTime.now(), 4),
        Venta(UUID.randomUUID(), ConstantValues.testProducto6, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)
    )

    private fun crearVentasConMenosDeCincoProductos() = listOf(
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
        Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 9)
    )

    private fun crearVentasParaIngresosCadena() = listOf(
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
        Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado1, LocalDateTime.now(), 5),
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 7),
        Venta(UUID.randomUUID(), ConstantValues.testProducto3, ConstantValues.testSupermercado2, LocalDateTime.now(), 12)
    )

    private fun crearVentasParaSupermercadoMayoresIngresos() = listOf(
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado2, LocalDateTime.now(), 15),
        Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5)
    )

    private fun crearVentasParaSupermercadoMayoresIngresosEmpate() = listOf(
        Venta(UUID.randomUUID(), ConstantValues.testProducto1, ConstantValues.testSupermercado1, LocalDateTime.now(), 10),
        Venta(UUID.randomUUID(), ConstantValues.testProducto2, ConstantValues.testSupermercado2, LocalDateTime.now(), 5),
    )

    private fun noVentasMessage(cadenaId: UUID): String {
        return String.format(ConstantValues.NO_VENTAS_CADENA_MESSAGE, cadenaId)
    }

    private fun supermercadoIngresosTotalesMessage(supermercadoNombre : String, supermercadoId: UUID, ingresos: Double): String {
        return String.format(ConstantValues.SUPERMERCADO_INGRESOS_TOTALES_MESSAGE, supermercadoNombre, supermercadoId, ingresos)
    }
}

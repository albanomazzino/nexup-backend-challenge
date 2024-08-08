import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.cadenaSupermercados.*
import com.gestionSupermercados.producto.Producto
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*

class CadenaSupermercadosOutputFormatterImplTest {
    private lateinit var productoService : ProductoService
    private lateinit var cadenaSupermercadosOutputFormatterImpl : CadenaSupermercadosOutputFormatter

    @BeforeEach
    fun setUp() {
        productoService = mock(ProductoService::class.java)
        cadenaSupermercadosOutputFormatterImpl = CadenaSupermercadosOutputFormatterImpl(productoService)
    }

    @Test
    fun formatTopProductosTest() {
        val topProductos = listOf(
            AbstractMap.SimpleEntry(ConstantValues.testProducto1, 5),
            AbstractMap.SimpleEntry(ConstantValues.testProducto2, 3)
        )

        val producto1 = Producto(ConstantValues.testProducto1, "Producto 1", 10.0)
        val producto2 = Producto(ConstantValues.testProducto2, "Producto 2", 15.0)

        `when`(productoService.getProductoById(ConstantValues.testProducto1)).thenReturn(producto1)
        `when`(productoService.getProductoById(ConstantValues.testProducto2)).thenReturn(producto2)

        val topProductosFormateado = cadenaSupermercadosOutputFormatterImpl.formatTopProductos(topProductos)

        assertEquals("Producto 1: 5-Producto 2: 3", topProductosFormateado)
    }


    @Test
    fun formatSupermercadoConMasIngresosTest() {
        val supermercadosCadena = listOf(
            Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes")),
            Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        )

        val testIngresos = 3500.0
        val supermercadoConMasIngresosFormateado = cadenaSupermercadosOutputFormatterImpl.formatSupermercadoConMasIngresos(ConstantValues.testSupermercado1, testIngresos, supermercadosCadena)

        assertEquals("Supermercado A (${ConstantValues.testSupermercado1}). Ingresos totales: $testIngresos", supermercadoConMasIngresosFormateado)
    }

    @Test
    fun formatSupermercadosAbiertosTest() {
        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 =  Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercadosAbiertos = listOf(supermercado1, supermercado2)

        val supermercadosAbiertosFormateado = cadenaSupermercadosOutputFormatterImpl.formatSupermercadosAbiertos(supermercadosAbiertos)

        assertEquals("${supermercado1.nombre} (${supermercado1.id}), ${supermercado2.nombre} (${supermercado2.id})", supermercadosAbiertosFormateado)
    }
}
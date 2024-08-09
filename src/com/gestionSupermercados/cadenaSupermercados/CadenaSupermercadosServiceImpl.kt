package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.venta.Venta
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*


/**
 * Servicio para gestionar operaciones relacionadas con cadenas de supermercados.
 */
interface CadenaSupermercadosService {
    /**
     * Añade una nueva cadena de supermercados.
     *
     * @param cadenaSupermercados La cadena de supermercados a añadir.
     */
    fun addCadenaSupermercados(cadenaSupermercados : CadenaSupermercados)

    /**
     * Obtiene los cinco productos más vendidos en una cadena de supermercados específica.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @return Una cadena de texto que representa los cinco productos más vendidos, en el formato:
     *         <nombre_producto1>: cantidad_vendida-<nombre_producto2>: cantidad_vendida-...-<nombre_producto5>: cantidad_vendida
     */
    fun getCincoProductosMasVendidos(cadenaSupermercadosId: UUID): String

    /**
     * Obtiene los ingresos totales de todos los supermercados en una cadena específica.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @return El total de ingresos generados por todos los supermercados en la cadena.
     */
    fun getIngresosTotalesSupermercados(cadenaSupermercadosId: UUID): Double

    /**
     * Obtiene el supermercado con mayores ingresos en una cadena específica.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @return Una cadena de texto que representa el supermercado con mayores ingresos, en el formato:
     *         <nombre_supermercado> (<id_supermercado>). Ingresos totales: <ingresos_totales>
     */
    fun getSupermercadoMayoresIngresos(cadenaSupermercadosId: UUID): String

    /**
     * Obtiene todos los supermercados abiertos, para una cadena específica, en un horario y día determinado.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @param hora Hora en que se verifica qué supermercados están abiertos.
     * @param dia Día en que se verifica qué supermercados están abiertos.
     * @return Una cadena de texto que representa todos los supermercados abiertos, en el formato:
     *         <nombre_supermercado1> (<id_supermercado1>), <nombre_supermercado2> (<id_supermercado2>), ...
     */
    fun getAllSupermercadosAbiertos(cadenaSupermercadosId: UUID, hora: Int, dia: String): String
}

class CadenaSupermercadosServiceImpl(
    private val cadenaSupermercadosRepository: CadenaSupermercadosRepository,
    private val ventaService: VentaService,
    private val productoService: ProductoService,
    private val outputFormatter: CadenaSupermercadosOutputFormatter
) : CadenaSupermercadosService {
    override fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados) {
        validarCadenaSupermercados(cadenaSupermercados)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadenaSupermercados)
    }

    override fun getCincoProductosMasVendidos(cadenaSupermercadosId : UUID): String {
        val supermercadosCadenaIDs = getSupermercadosCadenaIDs(cadenaSupermercadosId)
        val ventasCadenaSupermercados = filtrarVentasBySupermercados(supermercadosCadenaIDs)

        if (noSeRegistraronVentas(ventasCadenaSupermercados)) return "No hay ventas registradas para la cadena de supermercados $cadenaSupermercadosId."

        val cantidadVentasPorProducto = cantidadVentasPorProducto(ventasCadenaSupermercados)
        val cantidadVentasPorProductoEnOrden = ordenarProductosPorCantidad(cantidadVentasPorProducto)
        val top5ProductosMasVendidos = getTopNProductos(cantidadVentasPorProductoEnOrden, 5)

        return outputFormatter.formatTopProductos(top5ProductosMasVendidos)
    }

    override fun getIngresosTotalesSupermercados(cadenaSupermercadosId: UUID): Double {
        val supermercadosCadenaIDs = getSupermercadosCadenaIDs(cadenaSupermercadosId)
        val ventasCadenaSupermercados = filtrarVentasBySupermercados(supermercadosCadenaIDs)
        val cantidadVentasPorProducto = cantidadVentasPorProducto(ventasCadenaSupermercados)

        return calcularIngresos(cantidadVentasPorProducto)
    }

    override fun getSupermercadoMayoresIngresos(cadenaSupermercadosId: UUID): String {
        val supermercadosCadena = getSupermercadosCadena(cadenaSupermercadosId)
        val supermercadosCadenaIDs = getSupermercadosCadenaIDs(cadenaSupermercadosId)
        val ventasCadenaSupermercados = filtrarVentasBySupermercados(supermercadosCadenaIDs)
        val ingresosPorSupermercado = calcularIngresosPorSupermercado(ventasCadenaSupermercados)
        val supermercadoConMasIngresos = getSupermercadoConMasIngresos(ingresosPorSupermercado)

        return if (supermercadoConMasIngresos == null) {
            "No hay ventas registradas para la cadena de supermercados $cadenaSupermercadosId."
        } else {
            outputFormatter.formatSupermercadoConMasIngresos(supermercadoConMasIngresos.key, supermercadoConMasIngresos.value, supermercadosCadena)
        }
    }

    override fun getAllSupermercadosAbiertos(cadenaSupermercadosId: UUID, hora : Int, dia : String) : String {
        val supermercadosAbiertos = getSupermercadosAbiertos(cadenaSupermercadosId, hora, dia)

        return if (supermercadosAbiertos.isNullOrEmpty())
            "No se encontraron supermercados abiertos."
        else
            outputFormatter.formatSupermercadosAbiertos(supermercadosAbiertos)
    }

    private fun validarCadenaSupermercados(cadenaSupermercados: CadenaSupermercados) {
        if (cadenaSupermercados.nombre.isBlank()) {
            throw IllegalArgumentException("El nombre de la cadena de supermercados no puede estar en blanco.")
        }

        if (cadenaSupermercados.supermercados.isEmpty()) {
            throw IllegalArgumentException("La lista de supermercados no puede estar vacía o ser nula.")
        }
    }

    private fun getSupermercadosAbiertos(cadenaSupermercadosId: UUID, hora: Int, dia: String): List<Supermercado>? {
        return cadenaSupermercadosRepository.getAllSupermercadosAbiertos(cadenaSupermercadosId, hora, dia)
    }

    private fun noSeRegistraronVentas(ventasCadenaSupermercados : List<Venta>): Boolean {
        return ventasCadenaSupermercados.isEmpty()
    }

    private fun getSupermercadosCadenaIDs(cadenaSupermercadosId: UUID): List<UUID> {
        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercadosCadena(cadenaSupermercadosId)
            ?: throw IllegalArgumentException(ConstantValues.CADENASUPERMERCADOS_NO_ENCONTRADA_MESSAGE)
        return supermercadosCadena.map { it.id }
    }

    private fun getSupermercadosCadena(cadenaSupermercadosId: UUID): List<Supermercado> {
        return cadenaSupermercadosRepository.getAllSupermercadosCadena(cadenaSupermercadosId)
            ?: throw IllegalArgumentException(ConstantValues.CADENASUPERMERCADOS_NO_ENCONTRADA_MESSAGE)
    }

    private fun filtrarVentasBySupermercados(supermercadoIds: List<UUID>): List<Venta> {
        return ventaService.getAllVentas().filter { it.supermercadoId in supermercadoIds }
    }

    private fun cantidadVentasPorProducto(ventas: List<Venta>): Map<UUID, Int> {
        return ventas.groupBy { it.productoId }
            .mapValues { ventaActual -> ventaActual.value.sumOf { it.cantidad } }
    }

    private fun ordenarProductosPorCantidad(cantidadVentasPorProducto: Map<UUID, Int>): List<Map.Entry<UUID, Int>> {
        return cantidadVentasPorProducto.entries.sortedByDescending { it.value }
    }

    private fun getTopNProductos(cantidadVentasPorProductoEnOrden: List<Map.Entry<UUID, Int>>, topN: Int): List<Map.Entry<UUID, Int>> {
        return cantidadVentasPorProductoEnOrden.take(topN)
    }

    private fun calcularIngresos(cantidadVentasPorProducto: Map<UUID, Int>): Double {
        var ingresosTotales = 0.0
        for ((productoId, cantidadVendida) in cantidadVentasPorProducto) {
            val productoActual = productoService.getProductoById(productoId)
            val precioProductoActual = productoActual.precio
            ingresosTotales += precioProductoActual * cantidadVendida
        }

        return ingresosTotales
    }

    private fun calcularIngresosPorSupermercado(ventasCadenaSupermercados: List<Venta>): Map<UUID, Double> {
        return ventasCadenaSupermercados.groupBy { it.supermercadoId }
            .mapValues { ventasPorSupermercado ->
                ventasPorSupermercado.value.groupBy { it.productoId }
                    .mapValues { ventasPorProducto ->
                        val totalCantidadVendida = ventasPorProducto.value.sumOf { it.cantidad }
                        val producto = productoService.getProductoById(ventasPorProducto.key)
                        totalCantidadVendida * producto.precio
                    }
                    .values.sum()
            }
    }

    private fun getSupermercadoConMasIngresos(ingresosPorSupermercado: Map<UUID, Double>): Map.Entry<UUID, Double>? {
        return ingresosPorSupermercado.maxByOrNull { it.value }
    }
}
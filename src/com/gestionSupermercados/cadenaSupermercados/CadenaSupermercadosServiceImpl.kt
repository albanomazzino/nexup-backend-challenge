package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.venta.Venta
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

interface CadenaSupermercadosService {
    fun addCadenaSupermercados(cadenaSupermercados : CadenaSupermercados)
    fun getCincoProductosMasVendidos(cadenaSupermercadosId: UUID): String
    fun getIngresosTotalesSupermercados(cadenaSupermercadosId: UUID): Double
    fun getSupermercadoMayoresIngresos(cadenaSupermercadosId: UUID): String
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

    /*
        0. Obtener los IDs de los supermercados de la cadena
        1. Filtrar las ventas que corresponden a estos supermercados
        2. Comprobamos si se registraron ventas
        3. Mapear las ventas para acumular la cantidad total vendida por producto
        4. Ordenar el mapa de productos por la cantidad vendida en orden descendente
        5. Obtener las primeras 5 posiciones del mapa
        6. Formatear y devolver el resultado
    */
    override fun getCincoProductosMasVendidos(cadenaSupermercadosId : UUID): String {
        val supermercadosCadenaIDs = getSupermercadosCadenaIDs(cadenaSupermercadosId)
        val ventasCadenaSupermercados = filtrarVentasBySupermercados(supermercadosCadenaIDs)

        if (noSeRegistraronVentas(ventasCadenaSupermercados)) return "No hay ventas registradas para la cadena de supermercados $cadenaSupermercadosId."

        val cantidadVentasPorProducto = cantidadVentasPorProducto(ventasCadenaSupermercados)
        val cantidadVentasPorProductoEnOrden = ordenarProductosPorCantidad(cantidadVentasPorProducto)
        val top5ProductosMasVendidos = getTopNProductos(cantidadVentasPorProductoEnOrden, 5)

        return outputFormatter.formatTopProductos(top5ProductosMasVendidos)
    }

    /*
       0. Obtener los IDs de los supermercados de la cadena
       1. Filtrar las ventas que corresponden a estos supermercados
       2. Mapear las ventas para acumular la cantidad total vendida por producto
       3. Obtener el precio de cada producto y calcular los ingresos totales
     */
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
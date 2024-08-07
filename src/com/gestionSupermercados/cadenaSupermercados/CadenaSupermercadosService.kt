package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.venta.VentaService
import java.util.*

class CadenaSupermercadosService(
    private val cadenaSupermercadosRepository: CadenaSupermercadosRepository,
    private val ventaService: VentaService,
    private val productoService: ProductoService
) {
    fun getCincoProductosMasVendidos(cadenaSupermercadosId : UUID): String {
        // 0. Obtener los IDs de los supermercados de la cadena
        val supermercadosCadenaIDs = cadenaSupermercadosRepository.getAllSupermercadosCadena(cadenaSupermercadosId).map { it.id }

        // 1. Filtrar las ventas que corresponden a estos supermercados
        val ventasCadenaSupermercados = ventaService.getAllVentas().filter { it.supermercadoId in supermercadosCadenaIDs }

        if (ventasCadenaSupermercados.isEmpty()) {
            return "No hay ventas registradas para la cadena de supermercados $cadenaSupermercadosId."
        }

        // 2. Mapear las ventas para acumular la cantidad total vendida por producto
        var cantidadVentasPorProducto = ventasCadenaSupermercados.groupBy { it.productoId }
            .mapValues { ventaActual -> ventaActual.value.sumOf { it.cantidad } }

        // 3. Ordenar el mapa de productos por la cantidad vendida en orden descendente
        val cantidadVentasPorProductoEnOrden = cantidadVentasPorProducto.entries.sortedByDescending { it.value }

        // 4. Obtener las primeras 5 posiciones del mapa
        val top5ProductosMasVendidos = cantidadVentasPorProductoEnOrden.take(5)

        // 5. Formatear y devolver el resultado
        return top5ProductosMasVendidos.joinToString("-") { (productoId, cantidadVendida) ->
            val producto = productoService.getProductoById(productoId)
            "${producto?.nombre}: $cantidadVendida"
        }
    }

    fun getIngresosTotalesSupermercados(cadenaSupermercadosId : UUID): Double {
        var ingresosTotales = 0.0

        // 0. Obtener los IDs de los supermercados de la cadena
        val supermercadosCadenaIDs = cadenaSupermercadosRepository.getAllSupermercadosCadena(cadenaSupermercadosId).map { it.id }

        // 1. Filtrar las ventas que corresponden a estos supermercados
        val ventasCadenaSupermercados = ventaService.getAllVentas().filter { it.supermercadoId in supermercadosCadenaIDs }

        // 2. Mapear las ventas para acumular la cantidad total vendida por producto
        val cantidadVentasPorProducto = ventasCadenaSupermercados.groupBy { it.productoId }
            .mapValues { ventaActual -> ventaActual.value.sumOf { it.cantidad } }

        // 3. Obtener el precio de cada producto y calcular los ingresos totales
        for ((productoId, cantidadVendida) in cantidadVentasPorProducto) {
            val productoActual = productoService.getProductoById(productoId)
            val precioProductoActual = productoActual?.precio ?: 0.0
            ingresosTotales += precioProductoActual * cantidadVendida
        }

        return ingresosTotales
    }

    fun getSupermercadoMayoresIngresos(cadenaSupermercadosId : UUID): String {
        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercadosCadena(cadenaSupermercadosId)
        val ventasCadenaSupermercados = ventaService.getAllVentas().filter { it.supermercadoId in supermercadosCadena.map { supermercado -> supermercado.id } }

        // Agrupar por supermercadoId
        val ingresosPorSupermercado = ventasCadenaSupermercados.groupBy { it.supermercadoId }
            .mapValues { supermercadoActual ->
                // Agrupar por productoId dentro del supermercado
                supermercadoActual.value.groupBy { it.productoId }
                    .mapValues { ventasProductoActual ->
                        // Obtener el total vendido por producto
                        val totalCantidadVendida = ventasProductoActual.value.sumOf { it.cantidad }
                        // Obtener el precio del producto una vez
                        val productoActual = productoService.getProductoById(ventasProductoActual.key)
                        var productoActualPrecio = 0.0
                        if (productoActual != null){
                            productoActualPrecio = productoActual.precio
                        }
                        else {
                            throw IllegalArgumentException("Hubo un error al buscar un producto entre las ventas de la cadena $cadenaSupermercadosId.")
                        }
                        // Calcular los ingresos por producto
                        totalCantidadVendida * productoActualPrecio
                    }
                    .values.sum() // Sumar los ingresos de todos los productos del supermercado
            }

        val supermercadoConMasIngresos = ingresosPorSupermercado.maxByOrNull { it.value }

        return if (supermercadoConMasIngresos != null) {
            val supermercado = supermercadosCadena.find { it.id == supermercadoConMasIngresos.key }
            "${supermercado?.nombre} (${supermercado?.id}). Ingresos totales: ${supermercadoConMasIngresos.value}"
        } else {
            "No hay ventas registradas para la cadena de supermercados $cadenaSupermercadosId."
        }
    }
}

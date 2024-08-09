package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

interface CadenaSupermercadosOutputFormatter {
    fun formatTopProductos(topProductos: List<Map.Entry<UUID, Int>>): String
    fun formatSupermercadoConMasIngresos(supermercadoId: UUID, ingresos: Double, supermercadosCadena: List<Supermercado>): String
    fun formatSupermercadosAbiertos(supermercadosAbiertos: List<Supermercado>): String
}

class CadenaSupermercadosOutputFormatterImpl (private val productoService: ProductoService) : CadenaSupermercadosOutputFormatter {
    override fun formatTopProductos(topProductos: List<Map.Entry<UUID, Int>>): String {
        return topProductos.joinToString("-") { (productoId, cantidadVendida) ->
            val producto = productoService.getProductoById(productoId)
            "${producto.nombre}: $cantidadVendida"
        }
    }

    override fun formatSupermercadoConMasIngresos(supermercadoId: UUID, ingresos: Double, supermercadosCadena: List<Supermercado>): String {
        val supermercado = supermercadosCadena.find { it.id == supermercadoId }
        return "${supermercado?.nombre} (${supermercado?.id}). Ingresos totales: $ingresos"
    }

    override fun formatSupermercadosAbiertos(supermercadosAbiertos: List<Supermercado>): String {
        return supermercadosAbiertos.joinToString(", ") { "${it.nombre} (${it.id})" }
    }
}
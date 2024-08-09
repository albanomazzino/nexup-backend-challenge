package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*


/**
 * Formatea mensajes de salida asociados a cadenas de supermercados
 */
interface CadenaSupermercadosOutputFormatter {
    /**
     * Formatea una colección de los productos más vendidos en la cadena.
     *
     * @param topProductos Lista de productos más vendidos, representados como pares de `UUID` (ID del producto) y `Int` (cantidad vendida).
     * @return Mensaje formateado que muestra los productos más vendidos y sus cantidades.
     */
    fun formatTopProductos(topProductos: List<Map.Entry<UUID, Int>>): String

    /**
     * Formatea la información del supermercado con más ingresos en la cadena.
     *
     * @param supermercadoId Id del supermercado con más ingresos.
     * @param ingresos Ingresos totales del supermercado.
     * @param supermercadosCadena Lista de supermercados en la cadena.
     * @return Mensaje formateado que muestra el supermercado con más ingresos.
     */
    fun formatSupermercadoConMasIngresos(supermercadoId: UUID, ingresos: Double, supermercadosCadena: List<Supermercado>): String

    /**
     * Formatea la lista de supermercados abiertos.
     *
     * @param supermercadosAbiertos Lista de supermercados abiertos.
     * @return Mensaje formateado que muestra los supermercados abiertos.
     */
    fun formatSupermercadosAbiertos(supermercadosAbiertos: List<Supermercado>): String
}

/**
 * Implementación de [CadenaSupermercadosOutputFormatter] para formatear mensajes de salida asociados a cadenas de supermercados.
 *
 * @property productoService Servicio para obtener información sobre productos.
 */
class CadenaSupermercadosOutputFormatterImpl (private val productoService: ProductoService) : CadenaSupermercadosOutputFormatter {
    /**
     * El formato de salida es una cadena en la que cada producto se representa como
     * `<nombre_producto>: cantidad_vendida`, y los productos están separados por guiones.
     *
     * Ejemplo de formato:
     * ```
     * Manzana: 100 - Plátano: 80 - Naranja: 60
     * ```
     */
    override fun formatTopProductos(topProductos: List<Map.Entry<UUID, Int>>): String {
        return topProductos.joinToString("-") { (productoId, cantidadVendida) ->
            val producto = productoService.getProductoById(productoId)
            "${producto.nombre}: $cantidadVendida"
        }
    }

    /**
     * El formato de salida es una cadena que incluye el nombre del supermercado, su ID y los ingresos totales.
     *
     ** Ejemplo de formato:
     *```
     *Supermercado A (123e4567-e89b-12d3-a456-426614174000). Ingresos totales: 15000.0
     *```
     * @param supermercadoId Id del supermercado con más ingresos.
     * @param ingresos Ingresos totales del supermercado.
     * @param supermercadosCadena Lista de supermercados en la cadena.
     * @return Mensaje formateado que muestra el supermercado con más ingresos.
     */
    override fun formatSupermercadoConMasIngresos(supermercadoId: UUID, ingresos: Double, supermercadosCadena: List<Supermercado>): String {
        val supermercado = supermercadosCadena.find { it.id == supermercadoId }
        return "${supermercado?.nombre} (${supermercado?.id}). Ingresos totales: $ingresos"
    }

    /**
     * El formato de salida es una cadena en la que cada supermercado se representa como
     * `<nombre_supermercado> (ID)`, y los supermercados están separados por comas.
     *
     * Ejemplo de formato:
     * ```
     * Supermercado A (123e4567-e89b-12d3-a456-426614174000), Supermercado B (123e4567-e89b-12d3-a456-426614174001)
     * ```
     *
     * @param supermercadosAbiertos Lista de supermercados abiertos.
     * @return Mensaje formateado que muestra los supermercados abiertos.
     */
    override fun formatSupermercadosAbiertos(supermercadosAbiertos: List<Supermercado>): String {
        return supermercadosAbiertos.joinToString(", ") { "${it.nombre} (${it.id})" }
    }
}
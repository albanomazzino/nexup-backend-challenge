package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime
import java.util.*

/**
 * Interfaz para el repositorio de ventas.
 */
interface VentaRepository {
    /**
     * Obtiene las ventas asociadas a un producto y un supermercado específicos.
     *
     * @param productoId Id del producto cuyas ventas se desean obtener.
     * @param supermercadoId Id del supermercado donde se realizaron las ventas.
     * @return Colección de ventas correspondientes al producto y supermercado especificados.
     */
    fun getVentasByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): List<Venta>

    /**
     * Obtiene todas las ventas registradas.
     *
     * @return Una lista de todas las ventas.
     */
    fun getAllVentas(): List<Venta>

    /**
     * Añade una nueva venta.
     *
     * @param productoId Id del producto vendido.
     * @param supermercadoId Id del supermercado donde se realizó la venta.
     * @param fecha Fecha y hora en que se realizó la venta.
     * @param cantidad Cantidad vendida del producto.
     */
    fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidad: Int)
}

class VentaRepositoryImpl : VentaRepository {
    private val ventas = mutableListOf<Venta>()

    override fun getVentasByProductoIdSupermercadoId(productoId : UUID, supermercadoId: UUID) : List<Venta> {
        return ventas.filter { it.productoId == productoId && it.supermercadoId == supermercadoId }
    }

    override fun getAllVentas() : List<Venta> {
        return ventas
    }

    override fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidad: Int) {
        ventas.add(Venta(UUID.randomUUID(), productoId, supermercadoId, fecha, cantidad))
    }
}
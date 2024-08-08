package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime
import java.util.*

class VentaRepository {
    private val ventas = mutableListOf<Venta>()

    fun getVentasByProductoIdSupermercadoId(productoId : UUID, supermercadoId: UUID) : List<Venta> {
        return ventas.filter { it.productoId == productoId && it.supermercadoId == supermercadoId }
    }

    fun getAllVentas() : List<Venta> {
        return ventas
    }

    fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidad: Int) {
        ventas.add(Venta(UUID.randomUUID(), productoId, supermercadoId, fecha, cantidad))
    }
}
package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime
import java.util.*

interface VentaRepository {
    fun getVentasByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): List<Venta>
    fun getAllVentas(): List<Venta>
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
package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime

class VentaRepository {
    private val ventas = mutableListOf<Venta>()
    private var lastId = 1L

    fun getVentasByProductoIdSupermercadoId (idProducto : Long, supermercadoId: Long) : List<Venta> {
        return ventas.filter { it.productoId == idProducto && it.supermercadoId == supermercadoId }
    }

    fun getAllVentas () : List<Venta> {
        return ventas
    }

    fun addVenta (productoId: Long, supermercadoId: Long, fecha: LocalDateTime, cantidad: Int){
        ventas.add(Venta(lastId, productoId, supermercadoId, fecha, cantidad))
        lastId++
    }
}
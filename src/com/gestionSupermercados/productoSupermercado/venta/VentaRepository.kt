package com.gestionSupermercados.productoSupermercado.venta

class VentaRepository {
    private val ventas = mutableListOf<Venta>()

    fun getVentasByProductoId (idProducto : Long) : List<Venta> {
        return ventas.filter { it.productoId == idProducto }
    }

    fun getAllVentas () : List<Venta> {
        return ventas
    }

    fun addVenta (venta : Venta){
        ventas.add(venta)
    }
}
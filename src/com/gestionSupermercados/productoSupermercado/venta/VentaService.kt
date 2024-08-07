package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import com.gestionSupermercados.producto.ProductoService
import java.time.LocalDateTime
import java.util.*

class VentaService (
    private val ventaRepository: VentaRepository,
    private val posesionService: PosesionService,
    private val productoService : ProductoService
) {
    fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidad: Int) : Double {
        val stockActual = posesionService.getStock(productoId, supermercadoId) ?: 0

        if (stockActual >= cantidad) {

            ventaRepository.addVenta(productoId, supermercadoId, fecha, cantidad)
            posesionService.updatePosesionStockByProductoIdSupermercadoId(productoId, supermercadoId, -cantidad)
        } else {
            throw IllegalArgumentException("No es posible vender $cantidad unidades, el stock es de $stockActual.")
        }

        val precioProductoVendido = productoService.getProductoById(productoId)?.precio
        if (precioProductoVendido != null) {
            return cantidad * precioProductoVendido
        }
        else {
            throw IllegalArgumentException("No se encontró el producto con ID $productoId.")
        }
    }

    fun getCantidadVendidaByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Int {
        return ventaRepository.getVentasByProductoIdSupermercadoId(productoId, supermercadoId).sumOf { it.cantidad }
    }

    fun getIngresosByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Double {
        val ventasProducto = ventaRepository.getVentasByProductoIdSupermercadoId(productoId, supermercadoId)
        val producto = productoService.getProductoById(productoId)

        if (producto != null){
            return ventasProducto.sumOf { it.cantidad * producto.precio }
        }
        else {
            throw IllegalArgumentException("No se encontró el producto con ID $productoId.")
        }
    }

    fun getAllVentas() : List<Venta> {
        return ventaRepository.getAllVentas()
    }

    fun getIngresosTotalesBySupermercadoId(supermercadoId: UUID): Double {
        val ventasSupermercado = ventaRepository.getAllVentas().filter { it.supermercadoId == supermercadoId }
        var ingresosTotales = 0.0
        for (venta in ventasSupermercado){
            val precioProductoVendido = productoService.getProductoById(venta.productoId)?.precio
            if (precioProductoVendido != null){
                ingresosTotales += precioProductoVendido * venta.cantidad
            }
        }

        return ingresosTotales
    }
}
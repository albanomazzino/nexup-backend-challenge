package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.productoSupermercado.posesion.PosesionService
import java.time.LocalDateTime
import java.util.*

interface VentaService {
    fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidad: Int): Double
    fun getCantidadVendidaByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Int
    fun getIngresosByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Double
    fun getAllVentas(): List<Venta>
    fun getIngresosTotalesBySupermercadoId(supermercadoId: UUID): Double
}

class VentaServiceImpl (
    private val ventaRepository: VentaRepository,
    private val posesionService: PosesionService,
    private val productoService : ProductoService
) : VentaService {
    override fun addVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidadVenta: Int): Double {
        validarStockDisponible(productoId, supermercadoId, cantidadVenta)
        registrarVenta(productoId, supermercadoId, fecha, cantidadVenta)
        actualizarStock(productoId, supermercadoId, cantidadVenta)
        return calcularIngresosVenta(productoId, cantidadVenta)
    }

    override fun getCantidadVendidaByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Int {
        return calcularCantidadVendida(productoId, supermercadoId)
    }

    override fun getIngresosByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Double {
        val ventasProductoSupermercado = getVentasPorProductoYSupermercado(productoId, supermercadoId)
        return calcularIngresosVentas(ventasProductoSupermercado, productoId)
    }

    override fun getAllVentas() : List<Venta> {
        return ventaRepository.getAllVentas()
    }

    override fun getIngresosTotalesBySupermercadoId(supermercadoId: UUID): Double {
        val ventasSupermercado = getVentasPorSupermercado(supermercadoId)
        return calcularIngresosTotales(ventasSupermercado)
    }

    private fun validarStockDisponible(productoId: UUID, supermercadoId: UUID, cantidadVenta: Int) {
        val stockActual = posesionService.getStock(productoId, supermercadoId)
        if (stockActual < cantidadVenta) {
            throw IllegalArgumentException("No es posible vender $cantidadVenta unidades, el stock es de $stockActual.")
        }
    }

    private fun registrarVenta(productoId: UUID, supermercadoId: UUID, fecha: LocalDateTime, cantidadVenta: Int) {
        ventaRepository.addVenta(productoId, supermercadoId, fecha, cantidadVenta)
    }

    private fun actualizarStock(productoId: UUID, supermercadoId: UUID, cantidadVenta: Int) {
        posesionService.updatePosesionStockByProductoIdSupermercadoId(productoId, supermercadoId, -cantidadVenta)
    }

    private fun calcularIngresosVenta(productoId: UUID, cantidadVenta: Int): Double {
        val precioProducto = productoService.getProductoById(productoId).precio
        return cantidadVenta * precioProducto
    }

    private fun calcularCantidadVendida(productoId: UUID, supermercadoId: UUID): Int {
        return ventaRepository.getVentasByProductoIdSupermercadoId(productoId, supermercadoId).sumOf { it.cantidad }
    }

    private fun getVentasPorProductoYSupermercado(productoId: UUID, supermercadoId: UUID): List<Venta> {
        return ventaRepository.getVentasByProductoIdSupermercadoId(productoId, supermercadoId)
    }

    private fun calcularIngresosVentas(ventas: List<Venta>, productoId: UUID): Double {
        val precioProducto = productoService.getProductoById(productoId).precio
        return ventas.sumOf { it.cantidad * precioProducto }
    }

    private fun getVentasPorSupermercado(supermercadoId: UUID): List<Venta> {
        return ventaRepository.getAllVentas().filter { it.supermercadoId == supermercadoId }
    }

    private fun calcularIngresosTotales(ventasSupermercado: List<Venta>): Double {
        var ingresosTotales = 0.0
        for (venta in ventasSupermercado){
            val precioProductoVendido = productoService.getProductoById(venta.productoId).precio
            ingresosTotales += precioProductoVendido * venta.cantidad
        }

        return ingresosTotales
    }
}
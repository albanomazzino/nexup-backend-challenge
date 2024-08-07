package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.producto.ProductoService
import com.gestionSupermercados.supermercado.SupermercadoService
import java.util.*

class PosesionService(
    private val posesionRepository: PosesionRepository,
    private val productoService: ProductoService,
    private val supermercadoService: SupermercadoService
) {
    fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int) {
        val producto = productoService.getProductoById(productoId)
        val supermercado = supermercadoService.getSupermercadoById(supermercadoId)
        if (producto != null && supermercado != null) {
            posesionRepository.addPosesion(
                Posesion(productoId, supermercadoId, stock)
            )
        } else {
            throw IllegalArgumentException("Producto o supermercado no encontrado.")
        }
    }

    fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad : Int) {
        posesionRepository.updatePosesionStockByProductoIdSupermercadoId(productoId, supermercadoId, cantidad)
    }

    fun getStock(productoId: UUID, supermercadoId: UUID): Int? {
        return posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)?.stock
    }
}
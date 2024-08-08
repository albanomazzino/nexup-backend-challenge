package com.gestionSupermercados.productoSupermercado.posesion

import java.util.*

class PosesionService(
    private val posesionRepository: PosesionRepository,
) {
    fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int) {
        posesionRepository.addPosesion(Posesion(productoId, supermercadoId, stock))
    }

    fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad : Int) {
        val posesionAModificar = posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)

        if (posesionAModificar != null){
            val nuevoStock = posesionAModificar.stock + cantidad
            if (nuevoStock >= 0){
                posesionAModificar.stock = nuevoStock
            }
            else {
                throw IllegalArgumentException("El stock no puede ser menor a 0.")
            }
        }
        else {
            throw IllegalArgumentException("Producto o supermercado no encontrado.")
        }
    }

    fun getStock(productoId: UUID, supermercadoId: UUID): Int {
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)

        if (posesion != null){
            return posesion.stock
        }
        else {
            throw IllegalArgumentException("Producto o supermercado no encontrado.")
        }
    }
}
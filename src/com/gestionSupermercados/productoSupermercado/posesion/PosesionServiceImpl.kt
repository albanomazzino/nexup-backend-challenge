package com.gestionSupermercados.productoSupermercado.posesion

import java.util.*

interface PosesionService {
    fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int)
    fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad: Int)
    fun getStock(productoId: UUID, supermercadoId: UUID): Int
}

class PosesionServiceImpl(private val posesionRepository: PosesionRepository) : PosesionService {
    override fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int) {
        posesionRepository.addPosesion(Posesion(productoId, supermercadoId, stock))
    }

    override fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad: Int) {
        val posesionAModificar = getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
        val nuevoStock = calcularNuevoStock(posesionAModificar.stock, cantidad)
        validarStock(nuevoStock)
        setStock(posesionAModificar, nuevoStock)
    }

    override fun getStock(productoId: UUID, supermercadoId: UUID): Int {
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)

        if (posesion != null){
            return posesion.stock
        }
        else {
            throw IllegalArgumentException("Producto o supermercado no encontrado.")
        }
    }

    private fun getPosesionByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Posesion {
        return posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
            ?: throw IllegalArgumentException("Producto o supermercado no encontrado.")
    }

    private fun calcularNuevoStock(stockActual: Int, cantidad: Int): Int {
        return stockActual + cantidad
    }

    private fun validarStock(nuevoStock: Int) {
        if (nuevoStock < 0) {
            throw IllegalArgumentException("El stock no puede ser menor a 0.")
        }
    }

    private fun setStock(posesionAModificar : Posesion, nuevoStock : Int) {
        posesionAModificar.stock = nuevoStock
    }
}
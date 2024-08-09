package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.ConstantValues
import java.util.*

/**
 * Interfaz para el servicio de gestión de la posesión de productos en supermercados.
 */
interface PosesionService {
    /**
     * Añade una nueva posesión de un producto en un supermercado con una cantidad inicial de stock.
     *
     * @param productoId Id del producto.
     * @param supermercadoId Id del supermercado.
     * @param stock Cantidad inicial de stock del producto en el supermercado.
     */
    fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int)

    /**
     * Actualiza la cantidad de stock de un producto en un supermercado específico.
     *
     * @param productoId Id del producto.
     * @param supermercadoId Id del supermercado.
     * @param cantidad Cantidad a añadir o restar del stock actual. Puede ser negativa.
     */
    fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad: Int)

    /**
     * Obtiene el stock actual de un producto en un supermercado específico.
     *
     * @param productoId Id del producto.
     * @param supermercadoId Id del supermercado.
     * @return Cantidad de stock del producto en el supermercado.
     */
    fun getStock(productoId: UUID, supermercadoId: UUID): Int
}

class PosesionServiceImpl(private val posesionRepository: PosesionRepository) : PosesionService {
    override fun addPosesion(productoId: UUID, supermercadoId: UUID, stock: Int) {
        validarStock(stock)
        posesionRepository.addPosesion(Posesion(productoId, supermercadoId, stock))
    }

    override fun updatePosesionStockByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID, cantidad: Int) {
        val posesionAModificar = getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
        val nuevoStock = calcularNuevoStock(posesionAModificar.stock, cantidad)
        validarStock(nuevoStock)
        setStock(posesionAModificar, nuevoStock)
    }

    override fun getStock(productoId: UUID, supermercadoId: UUID): Int {
        val posesion = getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
        return posesion.stock
    }

    private fun getPosesionByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Posesion {
        return posesionRepository.getPosesionByProductoIdSupermercadoId(productoId, supermercadoId)
            ?: throw IllegalArgumentException(ConstantValues.PRODUCTO_O_SUPERMERCADO_NO_ENCONTRADO_MESSAGE)
    }

    private fun calcularNuevoStock(stockActual: Int, cantidad: Int): Int {
        return stockActual + cantidad
    }

    private fun validarStock(nuevoStock: Int) {
        if (nuevoStock < 0) {
            throw IllegalArgumentException(ConstantValues.STOCK_NEGATIVO_MESSAGE)
        }
    }

    private fun setStock(posesionAModificar : Posesion, nuevoStock : Int) {
        posesionAModificar.stock = nuevoStock
    }
}
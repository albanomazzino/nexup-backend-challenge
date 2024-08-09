package com.gestionSupermercados.productoSupermercado.posesion

import java.util.*

/**
 * Interfaz para el repositorio de posesión de productos en supermercados.
 */
interface PosesionRepository {
    /**
     * Obtiene la posesión de un producto en un supermercado específico.
     *
     * @param productoId Id del producto.
     * @param supermercadoId Id del supermercado.
     * @return La posesión del producto en el supermercado.
     */
    fun getPosesionByProductoIdSupermercadoId(productoId: UUID, supermercadoId: UUID): Posesion?

    /**
     * Añade una nueva posesión.
     *
     * @param posesion Posesión a añadir.
     */
    fun addPosesion(posesion: Posesion)
}

class PosesionRepositoryImpl : PosesionRepository {
    private val productosSupermercado = mutableListOf<Posesion>()

    override fun getPosesionByProductoIdSupermercadoId(productoId : UUID, supermercadoId: UUID) : Posesion? {
        return productosSupermercado.find { it.productoId == productoId && it.supermercadoId == supermercadoId}
    }

    override fun addPosesion(posesion : Posesion) {
        productosSupermercado.add(posesion)
    }
}
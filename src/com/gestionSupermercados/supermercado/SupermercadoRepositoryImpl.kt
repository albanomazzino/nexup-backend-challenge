package com.gestionSupermercados.supermercado

import java.util.*

/**
 * Interfaz para el repositorio de supermercados.
 */
interface SupermercadoRepository {
    /**
     * Obtiene un supermercado por su ID.
     *
     * @param id Id del supermercado a buscar.
     * @return El supermercado correspondiente al id proporcionado.
     */
    fun getSupermercadoById(id: UUID): Supermercado?

    /**
     * Añade un nuevo supermercado al repositorio.
     *
     * @param supermercado El supermercado a añadir.
     */
    fun addSupermercado(supermercado: Supermercado)
}

class SupermercadoRepositoryImpl : SupermercadoRepository {
    private val supermercados = mutableListOf<Supermercado>()

    override fun getSupermercadoById(id : UUID) : Supermercado? {
        return supermercados.find { it.id == id }
    }

    override fun addSupermercado(supermercado : Supermercado) {
        supermercados.add(supermercado)
    }
}
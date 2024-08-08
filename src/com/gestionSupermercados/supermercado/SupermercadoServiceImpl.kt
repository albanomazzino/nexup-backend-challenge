package com.gestionSupermercados.supermercado

import java.util.*

interface SupermercadoService {
    fun addSupermercado(supermercado: Supermercado)
    fun getSupermercadoById(id: UUID): Supermercado
}

class SupermercadoServiceImpl (private val supermercadoRepository : SupermercadoRepository) : SupermercadoService {
    override fun addSupermercado(supermercado: Supermercado) {
        supermercadoRepository.addSupermercado(supermercado)
    }

    override fun getSupermercadoById(id: UUID): Supermercado {
        return supermercadoRepository.getSupermercadoById(id)
            ?: throw NoSuchElementException("Supermercado con ID $id no encontrado.")
    }
}
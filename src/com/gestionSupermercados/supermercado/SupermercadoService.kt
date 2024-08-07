package com.gestionSupermercados.supermercado

import java.util.*

class SupermercadoService (private val supermercadoRepository : SupermercadoRepository) {
    fun addSupermercado(supermercado: Supermercado) {
        supermercadoRepository.addSupermercado(supermercado)
    }

    fun getSupermercadoById(id: UUID): Supermercado? {
        return supermercadoRepository.getSupermercadoById(id)
    }
}
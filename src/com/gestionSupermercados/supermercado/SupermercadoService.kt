package com.gestionSupermercados.supermercado

class SupermercadoService (private val supermercadoRepository : SupermercadoRepository) {
    fun addSupermercado(supermercado: Supermercado) {
        supermercadoRepository.addSupermercado(supermercado)
    }

    fun getSupermercadoById(id: Long): Supermercado? {
        return supermercadoRepository.getSupermercadoById(id)
    }
}
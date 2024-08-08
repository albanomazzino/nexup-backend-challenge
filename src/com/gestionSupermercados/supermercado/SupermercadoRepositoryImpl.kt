package com.gestionSupermercados.supermercado

import java.util.*

interface SupermercadoRepository {
    fun getSupermercadoById(id: UUID): Supermercado?
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
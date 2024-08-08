package com.gestionSupermercados.supermercado

import java.util.*

class SupermercadoRepository {
    private val supermercados = mutableListOf<Supermercado>()

    fun getSupermercadoById(id : UUID) : Supermercado? {
        return supermercados.find { it.id == id }
    }

    fun addSupermercado(supermercado : Supermercado) {
        supermercados.add(supermercado)
    }
}
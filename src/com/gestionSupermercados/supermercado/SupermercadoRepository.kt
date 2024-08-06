package com.gestionSupermercados.supermercado

class SupermercadoRepository {
    val supermercados = mutableListOf<Supermercado>()

    fun getSupermercadoById(id : Long) : Supermercado?{
        return supermercados.find { it.id == id }
    }

    fun addSupermercado(supermercado : Supermercado) {
        supermercados.add(supermercado)
    }
}
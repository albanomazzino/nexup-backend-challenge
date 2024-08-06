package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado

class CadenaSupermercadosRepository {
    private val supermercados = mutableListOf<Supermercado>()

    fun getAllSupermercados() : List<Supermercado> {
        return supermercados
    }

    fun getAllSupermercadosAbiertos(hora: Int, dia :String) : List<Supermercado> {
        return supermercados.filter { it.horarioApertura >= hora && hora <= it.horarioCierre && it.diasAbierto.contains(dia) }
    }

    fun addSupermercado(supermercado : Supermercado) {
        supermercados.add(supermercado)
    }
}
package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado

class CadenaSupermercadosRepository {
    private val supermercados = mutableListOf<Supermercado>()

    fun getAllSupermercados() : List<Supermercado> {
        return supermercados
    }

    fun getAllSupermercadosAbiertos(horaApertura : String, horaCierre : String, diasAbierto : List<String>) : List<Supermercado> {
        return supermercados.filter { it.horarioApertura == horaApertura && it.horarioCierre == horaCierre && it.diasAbierto == diasAbierto }
    }

    fun addSupermercado(supermercado : Supermercado) {
        supermercados.add(supermercado)
    }
}
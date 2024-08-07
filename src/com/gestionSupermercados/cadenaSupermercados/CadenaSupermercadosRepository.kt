package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

class CadenaSupermercadosRepository {
    private val cadenaSupermercados = mutableListOf<CadenaSupermercados>()

    fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados) {
        this.cadenaSupermercados.add(cadenaSupermercados)
    }

    fun getAllSupermercadosCadena(idCadenaSupermercados: UUID) : List<Supermercado> {
        return getCadenaSupermercadosById(idCadenaSupermercados).supermercados
    }

    fun getCadenaSupermercadosById(id : UUID) : CadenaSupermercados {
        val cadenaSupermercados = cadenaSupermercados.find { it.id == id }
        if (cadenaSupermercados != null){
            return cadenaSupermercados
        }
        else {
            throw IllegalArgumentException ("No se encontró una cadena de supermercados con el id $id.")
        }
    }

    fun getAllSupermercadosAbiertos(cadenaSupermercadosId : UUID, hora: Int, dia :String) : List<Supermercado> {
        val supermercadosCadena = getAllSupermercadosCadena(cadenaSupermercadosId)
        return supermercadosCadena.filter { it.horarioApertura <= hora && hora <= it.horarioCierre && it.diasAbierto.contains(dia) }
    }

    fun addSupermercado(cadenaSupermercadosId : UUID, supermercado : Supermercado) {
        getCadenaSupermercadosById(cadenaSupermercadosId).supermercados.add(supermercado)
    }
}
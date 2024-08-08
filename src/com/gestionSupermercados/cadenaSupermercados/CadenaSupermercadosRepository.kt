package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

class CadenaSupermercadosRepository {
    private val cadenaSupermercados = mutableListOf<CadenaSupermercados>()

    fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados) {
        this.cadenaSupermercados.add(cadenaSupermercados)
    }

    fun getAllSupermercadosCadena(idCadenaSupermercados: UUID) : List<Supermercado>? {
        return getCadenaSupermercadosById(idCadenaSupermercados)?.supermercados
    }

    fun getCadenaSupermercadosById(id : UUID) : CadenaSupermercados? {
        return cadenaSupermercados.find { it.id == id }
    }

    fun getAllSupermercadosAbiertos(cadenaSupermercadosId : UUID, hora: Int, dia :String) : List<Supermercado>? {
        val supermercadosCadena = getAllSupermercadosCadena(cadenaSupermercadosId)
        if (supermercadosCadena != null){
            return supermercadosCadena.filter { it.horarioApertura <= hora && hora <= it.horarioCierre && it.diasAbierto.contains(dia) }
        }
        return null
    }

    fun addSupermercado(cadenaSupermercadosId: UUID, supermercado: Supermercado) {
        val cadenaSupermercados = getCadenaSupermercadosById(cadenaSupermercadosId)
        if (cadenaSupermercados != null) {
            cadenaSupermercados.supermercados.add(supermercado)
        } else {
            throw IllegalArgumentException("Cadena de supermercados no encontrada.")
        }
    }
}
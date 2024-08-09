package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

interface CadenaSupermercadosRepository {
    fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados)
    fun getAllSupermercadosCadena(idCadenaSupermercados: UUID): List<Supermercado>?
    fun getCadenaSupermercadosById(id: UUID): CadenaSupermercados?
    fun getAllSupermercadosAbiertos(cadenaSupermercadosId: UUID, hora: Int, dia: String): List<Supermercado>?
    fun addSupermercado(cadenaSupermercadosId: UUID, supermercado: Supermercado)
}

class CadenaSupermercadosRepositoryImpl : CadenaSupermercadosRepository {
    private val cadenaSupermercados = mutableListOf<CadenaSupermercados>()

    override fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados) {
        this.cadenaSupermercados.add(cadenaSupermercados)
    }

    override fun getAllSupermercadosCadena(idCadenaSupermercados: UUID) : List<Supermercado>? {
        return getCadenaSupermercadosById(idCadenaSupermercados)?.supermercados
    }

    override fun getCadenaSupermercadosById(id : UUID) : CadenaSupermercados? {
        return cadenaSupermercados.find { it.id == id }
    }

    override fun getAllSupermercadosAbiertos(cadenaSupermercadosId : UUID, hora: Int, dia :String) : List<Supermercado>? {
        val supermercadosCadena = getAllSupermercadosCadena(cadenaSupermercadosId)
        if (supermercadosCadena != null){
            return supermercadosCadena.filter { it.horarioApertura <= hora && hora <= it.horarioCierre && it.diasAbierto.contains(dia) }
        }
        return null
    }

    override fun addSupermercado(cadenaSupermercadosId: UUID, supermercado: Supermercado) {
        val cadenaSupermercados = getCadenaSupermercadosById(cadenaSupermercadosId)
        if (cadenaSupermercados != null) {
            cadenaSupermercados.supermercados.add(supermercado)
        } else {
            throw IllegalArgumentException(ConstantValues.CADENASUPERMERCADOS_NO_ENCONTRADA_MESSAGE)
        }
    }
}
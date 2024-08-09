package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

/**
 * Gestiona la persistencia de las cadenas de supermercados.
 */
interface CadenaSupermercadosRepository {
    /**
     * Añade una nueva cadena de supermercados.
     *
     * @param cadenaSupermercados
     */
    fun addCadenaSupermercados(cadenaSupermercados: CadenaSupermercados)

    /**
     * Obtiene todos los supermercados de una cadena específica.
     *
     * @param idCadenaSupermercados Id de la cadena de supermercados.
     * @return Lista de supermercados asociados a la cadena.
     */
    fun getAllSupermercadosCadena(idCadenaSupermercados: UUID): List<Supermercado>?

    /**
     * Obtiene una cadena de supermercados por su Id.
     *
     * @param id Id de la cadena de supermercados.
     * @return La cadena de supermercados correspondiente al ID.
     */
    fun getCadenaSupermercadosById(id: UUID): CadenaSupermercados?

    /**
     * Obtiene, para una cadena específica, todos los supermercados abiertos en un horario y día determinado.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @param hora Hora en la que se obtienen los supermercados abiertos.
     * @param dia Día en el que se obtienen los supermercados están abiertos.
     * @return Lista de supermercados abiertos.
     */
    fun getAllSupermercadosAbiertos(cadenaSupermercadosId: UUID, hora: Int, dia: String): List<Supermercado>?

    /**
     * Añade un supermercado a una cadena de supermercados específica.
     *
     * @param cadenaSupermercadosId Id de la cadena de supermercados.
     * @param supermercado El supermercado a añadir.
     * @throws IllegalArgumentException Si no se encuentra la cadena de supermercados con el id proporcionado.
     */
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
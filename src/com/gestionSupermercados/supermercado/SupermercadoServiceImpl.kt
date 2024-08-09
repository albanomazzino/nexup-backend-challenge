package com.gestionSupermercados.supermercado

import java.util.*

/**
 * Interfaz para el servicio de supermercados.
 */
interface SupermercadoService {
    /**
     * Añade un nuevo supermercado.
     *
     * @param supermercado El supermercado a añadir.
     */
    fun addSupermercado(supermercado: Supermercado)

    /**
     * Obtiene un supermercado por su ID.
     *
     * @param id El id del supermercado a buscar.
     * @return El supermercado correspondiente al id proporcionado.
     * @throws NoSuchElementException Si no se encuentra un supermercado con el id proporcionado.
     */
    fun getSupermercadoById(id: UUID): Supermercado
}

class SupermercadoServiceImpl (private val supermercadoRepository : SupermercadoRepository) : SupermercadoService {
    override fun addSupermercado(supermercado: Supermercado) {
        validarSupermercado(supermercado)
        supermercadoRepository.addSupermercado(supermercado)
    }

    override fun getSupermercadoById(id: UUID): Supermercado {
        return supermercadoRepository.getSupermercadoById(id)
            ?: throw NoSuchElementException("Supermercado con ID $id no encontrado.")
    }

    private fun validarSupermercado(supermercado: Supermercado) {
        if (supermercado.nombre.isBlank()){
            throw IllegalArgumentException("El nombre del supermercado no puede estar vacío.")
        }

        if (supermercado.horarioApertura !in 0..23) {
            throw IllegalArgumentException("La hora de apertura debe estar entre 0 y 23.")
        }
        if (supermercado.horarioCierre !in 0..23) {
            throw IllegalArgumentException("La hora de cierre debe estar entre 0 y 23.")
        }
        if (supermercado.horarioCierre <= supermercado.horarioApertura) {
            throw IllegalArgumentException("La hora de cierre debe ser mayor que la hora de apertura.")
        }
        if (supermercado.diasAbierto.isEmpty()) {
            throw IllegalArgumentException("El supermercado debe estar abierto al menos un día.")
        }

        val diasValidos = setOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        supermercado.diasAbierto.forEach { dia ->
            if (dia !in diasValidos) {
                throw IllegalArgumentException("Día no válido: $dia. Los días válidos son: $diasValidos")
            }
        }
    }
}
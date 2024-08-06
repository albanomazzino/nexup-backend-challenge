package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CadenaSupermercadosRepositoryTest {

    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepository

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = CadenaSupermercadosRepository()
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        cadenaSupermercadosRepository.addSupermercado(supermercado)

        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercados()

        assertEquals(1, supermercadosCadena.size)
        assertEquals(supermercado, supermercadosCadena[0])
    }

    @Test
    fun getAllSupermercadosAbiertosTest() {
        val supermercado1 = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(3, "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))
        cadenaSupermercadosRepository.addSupermercado(supermercado1)
        cadenaSupermercadosRepository.addSupermercado(supermercado2)
        cadenaSupermercadosRepository.addSupermercado(supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(15, "Viernes")

        assertEquals(2, supermercadosAbiertos.size)
        assertEquals(supermercado2, supermercadosAbiertos[0])
        assertEquals(supermercado3, supermercadosAbiertos[1])
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesTest() {
        val supermercado1 = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(3, "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))
        cadenaSupermercadosRepository.addSupermercado(supermercado1)
        cadenaSupermercadosRepository.addSupermercado(supermercado2)
        cadenaSupermercadosRepository.addSupermercado(supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(21, "Lunes")

        assertTrue(supermercadosAbiertos.isEmpty())
    }
}
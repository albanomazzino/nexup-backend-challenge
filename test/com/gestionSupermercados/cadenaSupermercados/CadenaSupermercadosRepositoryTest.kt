package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CadenaSupermercadosRepositoryTest {

    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepository

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = CadenaSupermercadosRepository()
        val cadena1 = CadenaSupermercados(1L, "Cadena A", mutableListOf())
        val cadena2 = CadenaSupermercados(2L, "Cadena B", mutableListOf())
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena1)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena2)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        cadenaSupermercadosRepository.addSupermercado(1L, supermercado)

        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercadosCadena(1L)

        assertEquals(1, supermercadosCadena.size)
        assertEquals(supermercado, supermercadosCadena[0])
    }

    @Test
    fun getAllSupermercadosAbiertosTest() {
        val supermercado1 = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(3, "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(1L, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(1L, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(1L, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(1L, 15, "Viernes")

        assertEquals(2, supermercadosAbiertos.size)
        assertEquals(supermercado2, supermercadosAbiertos[0])
        assertEquals(supermercado3, supermercadosAbiertos[1])
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesTest() {
        val supermercado1 = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(3, "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(1L, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(1L, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(1L, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(1L, 21, "Lunes")

        assertTrue(supermercadosAbiertos.isEmpty())
    }

    @Test
    fun getCadenaSupermercadosByIdTest() {
        val testId = 1L
        val cadenaSupermercados = cadenaSupermercadosRepository.getCadenaSupermercadosById(testId)

        assertNotNull(cadenaSupermercados)
        assertEquals(testId, cadenaSupermercados.id)
    }

    @Test
    fun getCadenaSupermercadosByIdWithNoOccurrencesTest() {
        val testId = 3L
        val exception = assertThrows<IllegalArgumentException> {
            cadenaSupermercadosRepository.getCadenaSupermercadosById(testId)
        }
        assertEquals("No se encontró una cadena de supermercados con el id $testId.", exception.message)
    }
}
package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstVals
import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CadenaSupermercadosRepositoryTest {

    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepository

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = CadenaSupermercadosRepository()
        val cadena1 = CadenaSupermercados(ConstVals.testCadena1, "Cadena A", mutableListOf())
        val cadena2 = CadenaSupermercados(ConstVals.testCadena2, "Cadena B", mutableListOf())
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena1)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena2)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado)

        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstVals.testCadena1)

        assertEquals(1, supermercadosCadena.size)
        assertEquals(supermercado, supermercadosCadena[0])
    }

    @Test
    fun getAllSupermercadosAbiertosTest() {
        val supermercado1 = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(UUID.randomUUID(), "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(UUID.randomUUID(), "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstVals.testCadena1, 15, "Viernes")

        assertEquals(2, supermercadosAbiertos.size)
        assertEquals(supermercado2, supermercadosAbiertos[0])
        assertEquals(supermercado3, supermercadosAbiertos[1])
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesTest() {
        val supermercado1 = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(UUID.randomUUID(), "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(UUID.randomUUID(), "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(ConstVals.testCadena1, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstVals.testCadena1, 21, "Lunes")

        assertTrue(supermercadosAbiertos.isEmpty())
    }

    @Test
    fun getCadenaSupermercadosByIdTest() {
        val cadenaSupermercados = cadenaSupermercadosRepository.getCadenaSupermercadosById(ConstVals.testCadena1)

        assertNotNull(cadenaSupermercados)
        assertEquals(ConstVals.testCadena1, cadenaSupermercados.id)
    }

    @Test
    fun getCadenaSupermercadosByIdWithNoOccurrencesTest() {
        val testId = UUID.randomUUID()
        val exception = assertThrows<IllegalArgumentException> {
            cadenaSupermercadosRepository.getCadenaSupermercadosById(testId)
        }
        assertEquals("No se encontró una cadena de supermercados con el id $testId.", exception.message)
    }
}
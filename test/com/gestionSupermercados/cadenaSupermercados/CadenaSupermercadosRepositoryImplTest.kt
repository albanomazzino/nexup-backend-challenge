package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ConstantValues
import com.gestionSupermercados.supermercado.Supermercado
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CadenaSupermercadosRepositoryImplTest {

    private lateinit var cadenaSupermercadosRepository: CadenaSupermercadosRepositoryImpl

    @BeforeEach
    fun setUp() {
        cadenaSupermercadosRepository = CadenaSupermercadosRepositoryImpl()
        val cadena1 = CadenaSupermercados(ConstantValues.testCadena1, "Cadena A", mutableListOf())
        val cadena2 = CadenaSupermercados(ConstantValues.testCadena2, "Cadena B", mutableListOf())
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena1)
        cadenaSupermercadosRepository.addCadenaSupermercados(cadena2)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado)

        val supermercadosCadena = cadenaSupermercadosRepository.getAllSupermercadosCadena(ConstantValues.testCadena1)

        assertNotNull(supermercadosCadena)
        assertEquals(1, supermercadosCadena?.size)
        assertEquals(supermercado, supermercadosCadena?.get(0))
    }

    @Test
    fun addSupermercadoWithNoOccurrencesTest() {
        val supermercado = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        val exception = assertThrows<IllegalArgumentException> {
            cadenaSupermercadosRepository.addSupermercado(UUID.randomUUID(), supermercado)
        }

        assertEquals("Cadena de supermercados no encontrada.", exception.message)
    }

    @Test
    fun getAllSupermercadosAbiertosTest() {
        val supermercado1 = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(UUID.randomUUID(), "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(UUID.randomUUID(), "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstantValues.testCadena1, 15, "Viernes")

        assertNotNull(supermercadosAbiertos)
        assertEquals(2, supermercadosAbiertos?.size)
        assertEquals(supermercado2, supermercadosAbiertos?.get(0))
        assertEquals(supermercado3, supermercadosAbiertos?.get(1))
    }

    @Test
    fun getAllSupermercadosAbiertosWithNoOccurrencesTest() {
        val supermercado1 = Supermercado(UUID.randomUUID(), "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(UUID.randomUUID(), "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        val supermercado3 = Supermercado(UUID.randomUUID(), "Supermercado C", 9, 22, listOf("Miercoles", "Viernes"))

        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado1)
        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado2)
        cadenaSupermercadosRepository.addSupermercado(ConstantValues.testCadena1, supermercado3)

        val supermercadosAbiertos = cadenaSupermercadosRepository.getAllSupermercadosAbiertos(ConstantValues.testCadena1, 21, "Lunes")

        assertNotNull(supermercadosAbiertos)
        if (supermercadosAbiertos != null) {
            assertTrue(supermercadosAbiertos.isEmpty())
        }
    }

    @Test
    fun getCadenaSupermercadosByIdTest() {
        val cadenaSupermercados = cadenaSupermercadosRepository.getCadenaSupermercadosById(ConstantValues.testCadena1)

        assertNotNull(cadenaSupermercados)
        assertEquals(ConstantValues.testCadena1, cadenaSupermercados?.id)
    }

    @Test
    fun getCadenaSupermercadosByIdWithNoOccurrencesTest() {
        val cadenaSupermercados = cadenaSupermercadosRepository.getCadenaSupermercadosById(UUID.randomUUID())

        assertNull(cadenaSupermercados)
    }
}
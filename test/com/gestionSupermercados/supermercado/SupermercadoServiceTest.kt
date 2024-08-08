package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class SupermercadoServiceTest {
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        supermercadoRepository = SupermercadoRepository()
        supermercadoService = SupermercadoService(supermercadoRepository)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercadoAgregado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        supermercadoService.addSupermercado(supermercadoAgregado)

        val supermercadoEncontrado = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)
        assertEquals(supermercadoAgregado, supermercadoEncontrado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))

        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        val supermercadoEncontrado1 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val testId = UUID.randomUUID()
        val exception = assertThrows(NoSuchElementException::class.java) {
            supermercadoService.getSupermercadoById(testId)
        }

        assertEquals("Supermercado con ID $testId no encontrado.", exception.message)
    }
}

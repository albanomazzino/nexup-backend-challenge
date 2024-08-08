package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class SupermercadoServiceImplTest {
    private lateinit var supermercadoService: SupermercadoService
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        supermercadoRepository = mock(SupermercadoRepository::class.java)
        supermercadoService = SupermercadoServiceImpl(supermercadoRepository)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercadoAgregado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        doNothing().`when`(supermercadoRepository).addSupermercado(supermercadoAgregado)
        supermercadoService.addSupermercado(supermercadoAgregado)

        verify(supermercadoRepository).addSupermercado(supermercadoAgregado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))

        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado1)
        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado2)).thenReturn(supermercado2)
        val supermercadoEncontrado1 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val testId = UUID.randomUUID()

        `when`(supermercadoRepository.getSupermercadoById(testId)).thenThrow(NoSuchElementException("Supermercado con ID $testId no encontrado."))
        val exception = assertThrows(NoSuchElementException::class.java) {
            supermercadoService.getSupermercadoById(testId)
        }

        assertEquals("Supermercado con ID $testId no encontrado.", exception.message)
    }
}

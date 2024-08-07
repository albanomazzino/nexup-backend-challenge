package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstVals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class SupermercadoRepositoryTest {
    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        supermercadoRepository = SupermercadoRepository()
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(ConstVals.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        supermercadoRepository.addSupermercado(supermercado)

        val supermercadoEncontrado = supermercadoRepository.getSupermercadoById(ConstVals.testSupermercado1)
        assertEquals(supermercado, supermercadoEncontrado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstVals.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstVals.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        supermercadoRepository.addSupermercado(supermercado1)
        supermercadoRepository.addSupermercado(supermercado2)

        val supermercadoEncontrado1 = supermercadoRepository.getSupermercadoById(ConstVals.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoRepository.getSupermercadoById(ConstVals.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val supermercadoEncontrado = supermercadoRepository.getSupermercadoById(UUID.randomUUID())

        assertNull(supermercadoEncontrado)
    }
}

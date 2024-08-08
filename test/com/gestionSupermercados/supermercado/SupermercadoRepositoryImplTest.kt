package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class SupermercadoRepositoryImplTest {
    private lateinit var supermercadoRepository: SupermercadoRepositoryImpl

    @BeforeEach
    fun setUp() {
        supermercadoRepository = SupermercadoRepositoryImpl()
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        supermercadoRepository.addSupermercado(supermercado)

        val supermercadoEncontrado = supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1)
        assertEquals(supermercado, supermercadoEncontrado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))
        supermercadoRepository.addSupermercado(supermercado1)
        supermercadoRepository.addSupermercado(supermercado2)

        val supermercadoEncontrado1 = supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val supermercadoEncontrado = supermercadoRepository.getSupermercadoById(UUID.randomUUID())

        assertNull(supermercadoEncontrado)
    }
}

package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstVals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
        val supermercadoAgregado = Supermercado(ConstVals.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        supermercadoService.addSupermercado(supermercadoAgregado)

        val supermercadoEncontrado = supermercadoService.getSupermercadoById(ConstVals.testSupermercado1)
        assertEquals(supermercadoAgregado, supermercadoEncontrado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstVals.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstVals.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))

        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        val supermercadoEncontrado1 = supermercadoService.getSupermercadoById(ConstVals.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoService.getSupermercadoById(ConstVals.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val supermercadoEncontrado = supermercadoService.getSupermercadoById(UUID.randomUUID())

        assertNull(supermercadoEncontrado)
    }
}

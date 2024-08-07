package com.gestionSupermercados.supermercado

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        supermercadoService.addSupermercado(supermercado)

        val supermercadoEncontrado = supermercadoService.getSupermercadoById(1)
        assertEquals(supermercado, supermercadoEncontrado)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))

        supermercadoService.addSupermercado(supermercado1)
        supermercadoService.addSupermercado(supermercado2)

        val supermercadoEncontrado1 = supermercadoService.getSupermercadoById(1)
        val supermercadoEncontrado2 = supermercadoService.getSupermercadoById(2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val supermercadoEncontrado = supermercadoService.getSupermercadoById(999)

        assertNull(supermercadoEncontrado)
    }
}

package com.gestionSupermercados.supermercado


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SupermercadoRepositoryTest {

    private lateinit var supermercadoRepository: SupermercadoRepository

    @BeforeEach
    fun setUp() {
        supermercadoRepository = SupermercadoRepository()
    }

    @Test
    fun addSupermercadoTest() {
        val supermercado = Supermercado(1, "Supermercado A", 8, 20, listOf("Lunes", "Martes", "Miércoles"))
        supermercadoRepository.addSupermercado(supermercado)

        val addedSupermercado = supermercadoRepository.getSupermercadoById(1)

        assertNotNull(addedSupermercado)
        assertEquals(supermercado, addedSupermercado)
    }
}
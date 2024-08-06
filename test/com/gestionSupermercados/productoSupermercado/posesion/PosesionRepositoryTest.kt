package com.gestionSupermercados.productoSupermercado.posesion

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PosesionRepositoryTest {
    private lateinit var posesionRepository: PosesionRepository

    @BeforeEach
    fun setUp() {
        posesionRepository = PosesionRepository()
    }

    @Test
    fun addPosesionTest() {
        val productoSupermercado = Posesion(1, 1, 100)
        posesionRepository.addPosesion(productoSupermercado)

        val addedPosesion = posesionRepository.getPosesionByProductoIdSupermercadoId(1, 1)

        assertNotNull(addedPosesion)
        assertEquals(productoSupermercado, addedPosesion)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val posesion = Posesion(1, 1, 100)
        posesionRepository.addPosesion(posesion)

        posesionRepository.updatePosesionStockByProductoIdSupermercadoId(1, 1, -50)

        val posesionConStockActualizado = posesionRepository.getPosesionByProductoIdSupermercadoId(1, 1)

        assertEquals(50, posesionConStockActualizado?.stock)
    }
}
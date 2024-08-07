package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.ConstVals
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
        val productoSupermercado = Posesion(ConstVals.testProducto1, ConstVals.testSupermercado1, 100)
        posesionRepository.addPosesion(productoSupermercado)

        val addedPosesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstVals.testProducto1, ConstVals.testSupermercado1)

        assertNotNull(addedPosesion)
        assertEquals(addedPosesion?.productoId, ConstVals.testProducto1)
        assertEquals(addedPosesion?.supermercadoId, ConstVals.testSupermercado1)
        assertEquals(addedPosesion?.stock, 100)
    }

    @Test
    fun updatePosesionStockByProductoIdSupermercadoIdTest() {
        val posesion = Posesion(ConstVals.testProducto1, ConstVals.testSupermercado1, 100)
        posesionRepository.addPosesion(posesion)

        posesionRepository.updatePosesionStockByProductoIdSupermercadoId(ConstVals.testProducto1, ConstVals.testSupermercado1, -50)

        val posesionConStockActualizado = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstVals.testProducto1, ConstVals.testSupermercado1)

        assertEquals(50, posesionConStockActualizado?.stock)
    }
}
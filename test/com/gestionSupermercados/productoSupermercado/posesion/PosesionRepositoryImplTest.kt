package com.gestionSupermercados.productoSupermercado.posesion

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class PosesionRepositoryImplTest {
    private lateinit var posesionRepository: PosesionRepositoryImpl

    @BeforeEach
    fun setUp() {
        posesionRepository = PosesionRepositoryImpl()
    }

    @Test
    fun getPosesionByProductoIdSupermercadoIdNotFoundTest() {
        val posesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, UUID.randomUUID())

        assertNull(posesion)
    }

    @Test
    fun addPosesionTest() {
        val productoSupermercado = Posesion(ConstantValues.testProducto1, ConstantValues.testSupermercado1, 100)
        posesionRepository.addPosesion(productoSupermercado)

        val addedPosesion = posesionRepository.getPosesionByProductoIdSupermercadoId(ConstantValues.testProducto1, ConstantValues.testSupermercado1)

        assertNotNull(addedPosesion)
        assertEquals(addedPosesion?.productoId, ConstantValues.testProducto1)
        assertEquals(addedPosesion?.supermercadoId, ConstantValues.testSupermercado1)
        assertEquals(addedPosesion?.stock, 100)
    }
}
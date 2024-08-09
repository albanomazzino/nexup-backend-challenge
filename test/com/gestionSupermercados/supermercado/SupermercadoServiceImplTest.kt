package com.gestionSupermercados.supermercado

import com.gestionSupermercados.ConstantValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class SupermercadoServiceImplTest {
    private lateinit var supermercadoService: SupermercadoService
    private val supermercadoRepository = mock(SupermercadoRepository::class.java)

    @BeforeEach
    fun setUp() {
        supermercadoService = SupermercadoServiceImpl(supermercadoRepository)
    }

    @Test
    fun addSupermercadoTest() {
        val supermercadoAgregado = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))

        doNothing().`when`(supermercadoRepository).addSupermercado(supermercadoAgregado)
        supermercadoService.addSupermercado(supermercadoAgregado)

        verify(supermercadoRepository).addSupermercado(supermercadoAgregado)
        verifyNoMoreInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithEmptyNameTest() {
        val supermercadoConNombreVacio = Supermercado(ConstantValues.testSupermercado1, "", 8, 20, listOf("Lunes", "Martes"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConNombreVacio)
        }

        assertEquals("El nombre del supermercado no puede estar vacío.", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithInvalidHoursCase1Test() {
        val supermercadoConHorasInvalidas = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 7, 24, listOf("Lunes", "Martes"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConHorasInvalidas)
        }

        assertEquals("La hora de cierre debe estar entre 0 y 23.", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithInvalidHoursCase2Test() {
        val supermercadoConHorasInvalidas = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", -1, 8, listOf("Lunes", "Martes"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConHorasInvalidas)
        }

        assertEquals("La hora de apertura debe estar entre 0 y 23.", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithInvalidHoursCase3Test() {
        val supermercadoConHorasInvalidas = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 22, 8, listOf("Lunes", "Martes"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConHorasInvalidas)
        }

        assertEquals("La hora de cierre debe ser mayor que la hora de apertura.", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithInvalidDaysTest() {
        val supermercadoConDiasVacios = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Miartes"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConDiasVacios)
        }

        assertEquals("Día no válido: Miartes. Los días válidos son: [Lunes, Martes, Miércoles, Jueves, Viernes, Sábado, Domingo]", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun addSupermercadoWithEmptyDaysTest() {
        val supermercadoConDiasVacios = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, emptyList())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            supermercadoService.addSupermercado(supermercadoConDiasVacios)
        }

        assertEquals("El supermercado debe estar abierto al menos un día.", exception.message)
        verifyNoInteractions(supermercadoRepository)
    }

    @Test
    fun getSupermercadoByIdTest() {
        val supermercado1 = Supermercado(ConstantValues.testSupermercado1, "Supermercado A", 8, 20, listOf("Lunes", "Martes"))
        val supermercado2 = Supermercado(ConstantValues.testSupermercado2, "Supermercado B", 15, 22, listOf("Jueves", "Viernes"))

        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado1)).thenReturn(supermercado1)
        `when`(supermercadoRepository.getSupermercadoById(ConstantValues.testSupermercado2)).thenReturn(supermercado2)
        val supermercadoEncontrado1 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado1)
        val supermercadoEncontrado2 = supermercadoService.getSupermercadoById(ConstantValues.testSupermercado2)

        assertEquals(supermercado1, supermercadoEncontrado1)
        assertEquals(supermercado2, supermercadoEncontrado2)

        verify(supermercadoRepository).getSupermercadoById(ConstantValues.testSupermercado1)
        verify(supermercadoRepository).getSupermercadoById(ConstantValues.testSupermercado2)
        verifyNoMoreInteractions(supermercadoRepository)
    }

    @Test
    fun getSupermercadoByIdWithNoOccurrencesTest() {
        val testId = UUID.randomUUID()

        `when`(supermercadoRepository.getSupermercadoById(testId))
            .thenThrow(NoSuchElementException(supermercadoNoEncontradoMessage(testId)))

        val exception = assertThrows(NoSuchElementException::class.java) {
            supermercadoService.getSupermercadoById(testId)
        }

        assertEquals(supermercadoNoEncontradoMessage(testId), exception.message)
        verify(supermercadoRepository).getSupermercadoById(testId)
        verifyNoMoreInteractions(supermercadoRepository)
    }

    private fun supermercadoNoEncontradoMessage(idSupermercado : UUID) : String{
        return String.format(ConstantValues.SUPERMERCADO_NO_ENCONTRADO_MESSAGE, idSupermercado)
    }
}

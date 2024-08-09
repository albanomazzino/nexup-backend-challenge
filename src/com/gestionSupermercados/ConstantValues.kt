package com.gestionSupermercados

import java.util.UUID

class ConstantValues {
    companion object {
        val testCadena1: UUID = UUID.randomUUID()
        val testCadena2: UUID = UUID.randomUUID()

        val testProducto1: UUID = UUID.randomUUID()
        val testProducto2: UUID = UUID.randomUUID()
        val testProducto3: UUID = UUID.randomUUID()
        val testProducto4: UUID = UUID.randomUUID()
        val testProducto5: UUID = UUID.randomUUID()
        val testProducto6: UUID = UUID.randomUUID()

        val testSupermercado1: UUID = UUID.randomUUID()
        val testSupermercado2: UUID = UUID.randomUUID()

        const val NO_VENTAS_CADENA_MESSAGE = "No hay ventas registradas para la cadena de supermercados %s."
        const val SUPERMERCADO_INGRESOS_TOTALES_MESSAGE = "%s (%s). Ingresos totales: %.1f"
        const val NO_SUPERMERCADOS_ABIERTOS_MESSAGE = "No se encontraron supermercados abiertos."
        const val PRODUCTO_NO_ENCONTRADO_MESSAGE = "Producto con ID %s no encontrado."
        const val SUPERMERCADO_NO_ENCONTRADO_MESSAGE = "Supermercado con ID %s no encontrado."
        const val PRODUCTO_O_SUPERMERCADO_NO_ENCONTRADO_MESSAGE = "Producto o supermercado no encontrado."
        const val STOCK_NEGATIVO_MESSAGE = "El stock no puede ser menor a 0."
        const val CADENASUPERMERCADOS_NO_ENCONTRADA_MESSAGE = "Cadena de supermercados no encontrada."
    }
}
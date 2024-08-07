package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime
import java.util.*

data class Venta (
    val id: UUID,
    val productoId : UUID,
    val supermercadoId : UUID,
    val fecha : LocalDateTime,
    val cantidad : Int,
)
package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime

data class Venta (
    val id: Long,
    val productoId : Long,
    val supermercadoId : Long,
    val fecha : LocalDateTime,
    val cantidad : Int,
)
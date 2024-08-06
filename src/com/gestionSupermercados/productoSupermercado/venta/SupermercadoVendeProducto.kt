package com.gestionSupermercados.productoSupermercado.venta

import java.time.LocalDateTime

data class SupermercadoVendeProducto (
    val productoId : Long,
    val supermercadoId : Long,
    val fecha : LocalDateTime,
    val cantidad : Int,
)
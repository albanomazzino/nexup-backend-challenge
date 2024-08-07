package com.gestionSupermercados.productoSupermercado.posesion

import java.util.UUID

data class Posesion (
    val productoId : UUID,
    val supermercadoId : UUID,
    var stock : Int
)
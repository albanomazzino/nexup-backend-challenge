package com.gestionSupermercados.productoSupermercado.posesion

data class Posesion (
    val productoId : Long,
    val supermercadoId : Long,
    var stock : Int
)
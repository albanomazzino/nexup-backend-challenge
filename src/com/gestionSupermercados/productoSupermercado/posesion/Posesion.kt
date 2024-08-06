package com.gestionSupermercados.productoSupermercado.posesion

data class Posesion (
    val supermercadoId : Long,
    val productoId : Long,
    var stock : Int
)
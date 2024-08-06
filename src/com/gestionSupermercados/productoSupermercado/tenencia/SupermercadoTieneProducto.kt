package com.gestionSupermercados.productoSupermercado.tenencia

data class SupermercadoTieneProducto (
    val supermercadoId : Long,
    val productoId : Long,
    var stock : Int
)
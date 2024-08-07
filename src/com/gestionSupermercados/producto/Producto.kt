package com.gestionSupermercados.producto

import java.util.*

data class Producto (
    val id: UUID,
    val nombre : String,
    val precio : Double
)
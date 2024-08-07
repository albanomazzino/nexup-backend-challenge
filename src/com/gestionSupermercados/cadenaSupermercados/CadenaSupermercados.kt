package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado

data class CadenaSupermercados (
    val id : Long,
    val nombre : String,
    val supermercados : MutableList<Supermercado>
)
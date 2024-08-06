package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado

data class CadenaSupermercados (
    val nombre : String,
    val id : Long,
    val supermercados : MutableList<Supermercado>
)
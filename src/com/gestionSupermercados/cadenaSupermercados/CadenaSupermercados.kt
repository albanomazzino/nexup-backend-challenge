package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.supermercado.Supermercado
import java.util.*

data class CadenaSupermercados (
    val id : UUID,
    val nombre : String,
    val supermercados : MutableList<Supermercado>
)
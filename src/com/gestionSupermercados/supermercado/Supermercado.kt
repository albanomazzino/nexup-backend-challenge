package com.gestionSupermercados.supermercado

import java.util.*

data class Supermercado (
    val id : UUID,
    val nombre : String,
    val horarioApertura : Int,
    val horarioCierre : Int,
    val diasAbierto : List<String>
)
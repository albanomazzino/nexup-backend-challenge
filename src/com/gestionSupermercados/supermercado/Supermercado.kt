package com.gestionSupermercados.supermercado

data class Supermercado (
    val id : Long,
    val nombre : String,
    val horarioApertura : Int,
    val horarioCierre : Int,
    val diasAbierto : List<String>
)
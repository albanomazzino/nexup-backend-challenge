package com.gestionSupermercados.supermercado

data class Supermercado (
    val id : Long,
    val nombre : String,
    val horarioApertura : String,
    val horarioCierre : String,
    val diasAbierto : List<String>
)
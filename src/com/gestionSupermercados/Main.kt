package com.gestionSupermercados

fun main() {
    init()
    println("Hello world")
}

private fun init() {
    val productoService = ServiceProvider.getProductoService()
    val supermercadoService = ServiceProvider.getSupermercadoService()
    val posesionService = ServiceProvider.getPosesionService()
    val ventaService = ServiceProvider.getVentaService()
    val cadenaSupermercadosService = ServiceProvider.getCadenaSupermercadosService()
}

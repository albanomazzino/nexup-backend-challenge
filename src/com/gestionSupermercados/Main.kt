package com.gestionSupermercados

fun main() {
    val cadenaSupermercadosService = ServiceProvider.getCadenaSupermercadosService()
    val productoService = ServiceProvider.getProductoService()
    val ventaService = ServiceProvider.getVentaService()
    val posesionService = ServiceProvider.getPosesionService()
    val supermercadoService = ServiceProvider.getSupermercadoService()

    println("Hello world")
}

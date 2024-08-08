package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.producto.ProductoProvider
import com.gestionSupermercados.productoSupermercado.venta.VentaProvider

object CadenaSupermercadosProvider {
    fun getCadenaSupermercadosService(): CadenaSupermercadosService {
        val cadenaSupermercadosRepository = getCadenaSupermercadosRepository()
        val ventaService = VentaProvider.getVentaService()
        val productoService = ProductoProvider.getProductoService()

        return CadenaSupermercadosService(cadenaSupermercadosRepository, ventaService, productoService)
    }

    private fun getCadenaSupermercadosRepository(): CadenaSupermercadosRepository {
        return CadenaSupermercadosRepositoryImpl()
    }
}
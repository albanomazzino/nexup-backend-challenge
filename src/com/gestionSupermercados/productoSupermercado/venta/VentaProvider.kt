package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.producto.ProductoProvider
import com.gestionSupermercados.productoSupermercado.posesion.PosesionProvider

object VentaProvider {
    fun getVentaService(): VentaService {
        val ventaRepository = getVentaRepository()
        val posesionService = PosesionProvider.getPosesionService()
        val productoService = ProductoProvider.getProductoService()

        return VentaServiceImpl(ventaRepository, posesionService, productoService)
    }

    private fun getVentaRepository() : VentaRepository{
        return VentaRepositoryImpl()
    }
}
package com.gestionSupermercados.productoSupermercado.venta

import com.gestionSupermercados.ServiceProvider

object VentaProvider {
    fun getVentaService(): VentaService {
        val productoService = ServiceProvider.getProductoService()
        val posesionService = ServiceProvider.getPosesionService()
        val ventaRepository = getVentaRepository()

        return VentaServiceImpl(ventaRepository, posesionService, productoService)
    }

    private fun getVentaRepository() : VentaRepository {
        return VentaRepositoryImpl()
    }
}
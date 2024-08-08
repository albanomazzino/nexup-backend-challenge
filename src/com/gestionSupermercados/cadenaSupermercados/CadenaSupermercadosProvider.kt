package com.gestionSupermercados.cadenaSupermercados

import com.gestionSupermercados.ServiceProvider
import com.gestionSupermercados.producto.ProductoService

object CadenaSupermercadosProvider {
    fun getCadenaSupermercadosService(): CadenaSupermercadosService {
        val cadenaSupermercadosRepository = getCadenaSupermercadosRepository()
        val ventaService = ServiceProvider.getVentaService()
        val productoService = ServiceProvider.getProductoService()
        val cadenaSupermercadosOutputFormatter = getCadenaSupermercadosOutputFormatter(productoService)

        return CadenaSupermercadosServiceImpl(cadenaSupermercadosRepository, ventaService, productoService, cadenaSupermercadosOutputFormatter)
    }

    private fun getCadenaSupermercadosRepository(): CadenaSupermercadosRepository {
        return CadenaSupermercadosRepositoryImpl()
    }

    private fun getCadenaSupermercadosOutputFormatter(productoService : ProductoService): CadenaSupermercadosOutputFormatter {
        return CadenaSupermercadosOutputFormatterImpl(productoService)
    }
}
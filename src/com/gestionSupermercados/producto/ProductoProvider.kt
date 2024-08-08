package com.gestionSupermercados.producto

object ProductoProvider {
    private val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl()
    }

    fun getProductoService(): ProductoService {
        return ProductoService(productoRepository)
    }
}
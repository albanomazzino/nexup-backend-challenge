package com.gestionSupermercados

import com.gestionSupermercados.cadenaSupermercados.CadenaSupermercadosProvider
import com.gestionSupermercados.producto.ProductoProvider
import com.gestionSupermercados.productoSupermercado.posesion.PosesionProvider
import com.gestionSupermercados.productoSupermercado.venta.VentaProvider
import com.gestionSupermercados.supermercado.SupermercadoProvider

object ServiceProvider {
    fun getCadenaSupermercadosService() = CadenaSupermercadosProvider.getCadenaSupermercadosService()

    fun getProductoService() = ProductoProvider.getProductoService()

    fun getVentaService() = VentaProvider.getVentaService()

    fun getPosesionService() = PosesionProvider.getPosesionService()

    fun getSupermercadoService() = SupermercadoProvider.getSupermercadoService()
}

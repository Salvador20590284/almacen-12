package com.salvador.almacen.mappers;

import com.salvador.almacen.dto.DetalleVenta.DetalleVentaRequest;
import com.salvador.almacen.dto.DetalleVenta.DetalleVentaResponse;
import com.salvador.almacen.entities.DetalleVenta;
import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.entities.Venta;
import org.springframework.stereotype.Component;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(
            DetalleVentaRequest request, Venta venta, Producto producto) {

        return request == null
                ? null
                : DetalleVenta.crear(
                venta,
                producto,
                request.cantidadProducto(),
                producto.getPrecio());
    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta detalleVenta) {

        return detalleVenta == null
                ? null
                : new DetalleVentaResponse(
                detalleVenta.getProducto().getId(),
                detalleVenta.getProducto().getNombre(),
                detalleVenta.getCantidadProducto(),
                detalleVenta.getPrecioProducto(),
                detalleVenta.calcularSubtotal());
    }
}
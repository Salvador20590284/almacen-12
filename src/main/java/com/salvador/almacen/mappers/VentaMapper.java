package com.salvador.almacen.mappers;

import com.salvador.almacen.dto.Ventas.VentaRequest;
import com.salvador.almacen.dto.Ventas.VentaResponse;
import com.salvador.almacen.entities.Sucursal;
import com.salvador.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private final SucursalMapper sucursalMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    public Venta requestAEntidad(VentaRequest request, Sucursal sucursal) {

        return request == null
                ? null
                : Venta.crear(sucursal);
    }

    public VentaResponse entidadAResponse(Venta venta) {

        return venta == null
                ? null
                : new VentaResponse(
                venta.getId(),
                venta.getFecha().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                venta.getDetalleVentas().stream()
                        .map(detalleVentaMapper::entidadAResponse)
                        .toList(),
                venta.calcularTotal()
        );
    }
}
package com.salvador.almacen.mappers;

import com.salvador.almacen.dto.productos.ProductoResponse;
import com.salvador.almacen.dto.sucursales.SucursalRequest;
import com.salvador.almacen.dto.sucursales.SucursalResponse;
import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public Sucursal requestAEntidad(SucursalRequest request){
        return request == null
                ? null
                :Sucursal.crear(request.nombre(),request.direccion());
    }

    public SucursalResponse entidadAResponse(Sucursal sucursal){
        return sucursal == null
                ? null
                : new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion());
    }
}

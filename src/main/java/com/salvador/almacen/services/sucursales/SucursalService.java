package com.salvador.almacen.services.sucursales;

import com.salvador.almacen.dto.sucursales.SucursalRequest;
import com.salvador.almacen.dto.sucursales.SucursalResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);
    SucursalResponse registrar(SucursalRequest request);
    SucursalResponse actualizar(SucursalRequest request, Long id);
    void eliminar(Long id);
}

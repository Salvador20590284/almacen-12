package com.salvador.almacen.services.Ventas;

import com.salvador.almacen.dto.Ventas.VentaRequest;
import com.salvador.almacen.dto.Ventas.VentaResponse;

import java.util.List;

public interface VentaService {
    List<VentaResponse> listar();
    List<VentaResponse> listarCanceladas();
    VentaResponse obtenerPorIdActiva(Long id);
    VentaResponse registrar(VentaRequest request);

    void cancelar(Long id);
}

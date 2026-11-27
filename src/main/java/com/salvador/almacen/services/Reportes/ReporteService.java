package com.salvador.almacen.services.Reportes;

import com.salvador.almacen.dto.Reportes.ReporteVentasSucursalResponse;

import java.util.List;

public interface ReporteService {

    List<ReporteVentasSucursalResponse> generarReportePorSucursal();

}
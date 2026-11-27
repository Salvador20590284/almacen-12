package com.salvador.almacen.services.Reportes;

import com.salvador.almacen.dto.Reportes.ReporteVentasSucursalResponse;
import com.salvador.almacen.enums.EstadoVenta;
import com.salvador.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;

    @Override
    public List<ReporteVentasSucursalResponse> generarReportePorSucursal() {

        log.info("Generando reporte de ventas por sucursal...");

        return ventaRepository.generarReportePorSucursal(
                EstadoVenta.REGISTRADA);
    }
}
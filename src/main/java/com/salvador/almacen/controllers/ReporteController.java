package com.salvador.almacen.controllers;

import com.salvador.almacen.docs.ProblemaDoc;
import com.salvador.almacen.dto.Reportes.ReporteVentasSucursalResponse;
import com.salvador.almacen.services.Reportes.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "API Reportes", description = "Gestión de reportes económicos")
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas-sucursal")
    @Operation(summary = "Reporte de ventas por sucursal",
            description = "Obtiene el total facturado y la cantidad de productos vendidos por sucursal, excluyendo ventas canceladas")
    @ApiResponse(responseCode = "200",
            description = "Reporte obtenido correctamente")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> generarReportePorSucursal() {

        return ResponseEntity.ok(
                reporteService.generarReportePorSucursal());
    }
}
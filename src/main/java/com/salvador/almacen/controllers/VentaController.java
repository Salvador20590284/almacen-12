package com.salvador.almacen.controllers;

import com.salvador.almacen.docs.ProblemaDoc;
import com.salvador.almacen.dto.Ventas.VentaRequest;
import com.salvador.almacen.dto.Ventas.VentaResponse;
import com.salvador.almacen.services.Ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "API Ventas", description = "Gestion de ventas")
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))

public class VentaController {
    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Listar ventas registradas",
            description = "Obtiene únicamente las ventas en estado REGISTRADA")
    @ApiResponse(responseCode = "200", description = "Listado de ventas registradas obtenido")
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/canceladas")
    @Operation(summary = "Listar ventas canceladas",
            description = "Obtiene el histórico de ventas en estado CANCELADA")
    @ApiResponse(responseCode = "200", description = "Listado de ventas canceladas obtenido")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {

        return ResponseEntity.ok(ventaService.listarCanceladas());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta activa por ID")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "La venta activa no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorIdActiva(

            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }


    @PostMapping
    @Operation(summary = "Registrar una nueva venta")
    @ApiResponse(responseCode = "201", description = "Venta creada")
    @ApiResponse(responseCode = "404", description = "La sucursal o el producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Stock insuficiente del producto",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(

            @Valid @RequestBody VentaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.registrar(request));
    }


    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una venta",
            description = "Cancela la venta y devuelve los productos al inventario")
    @ApiResponse(responseCode = "204", description = "Venta cancelada correctamente")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La venta ya está cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> cancelar(

            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        ventaService.cancelar(id);

        return ResponseEntity.noContent().build();
    }
}
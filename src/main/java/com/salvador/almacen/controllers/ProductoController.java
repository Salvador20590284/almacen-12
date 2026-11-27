package com.salvador.almacen.controllers;

import com.salvador.almacen.docs.ProblemaDoc;
import com.salvador.almacen.dto.productos.ProductoRequest;
import com.salvador.almacen.dto.productos.ProductoResponse;
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
import com.salvador.almacen.services.productos.ProductoService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "API Productos", description = "Gestion de inventario de productos")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Todos los filtros son opcionales")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<ProductoResponse>> listar(

            @Parameter(description = "Busqueda por nombre", example = "Laptop")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Busqueda por categoria", example = "Electronico")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "Busqueda por precio minimo", example = "1000")
            @RequestParam(required = false) BigDecimal precioMin,

            @Parameter(description = "Busqueda por precio Maximo", example = "200000")
            @RequestParam(required = false) BigDecimal precioMax
    ) {
        return ResponseEntity.ok(productoService.listar(nombre, categoria, precioMin, precioMax));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
    content = @Content(mediaType = "application/problem+json",
            schema = @Schema(implementation = ProblemaDoc.class)))

    public ResponseEntity<ProductoResponse> obtenerPorId(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevoi producto")
    @ApiResponse(responseCode = "201", description = "Producto Creado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse>registrar(
            @Valid @RequestBody ProductoRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con nuevos datos del producto",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))

    public ResponseEntity<ProductoResponse> actualizar(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,

            @Valid @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.ok(productoService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "200", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "El producto está en uso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))

    public ResponseEntity<ProductoResponse> eliminar(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
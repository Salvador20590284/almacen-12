package com.salvador.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de la sucursal")
public record SucursalResponse(

        @Schema(description = "ID unico de la sucursal", example = "1")
        Long id,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
        String nombre,

        @Schema(description = "Direccion de la sucursal", example = "Calle 5 #10")
        String direccion
        ) {}

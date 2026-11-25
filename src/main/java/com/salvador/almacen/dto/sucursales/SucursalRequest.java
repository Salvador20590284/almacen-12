package com.salvador.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar una sucursal")
public record SucursalRequest(

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
        @NotBlank(message= "El nombre es requerido")
@Size(min = 5, max = 50, message = "El nombre debe tener entre 5 y 50 caracteres")
String nombre,

@Schema(description = "Direccion de la sucursal", example = "Calle 5 #10")
@NotBlank(message= "La direccion es requerida")
@Size(min = 10, max = 150, message = "La direccion debe tener entre 5 y 50 caracteres")
String direccion

){}

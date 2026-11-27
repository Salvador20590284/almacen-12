package com.salvador.almacen.dto.Reportes;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Reporte económico de ventas por sucursal")
public record ReporteVentasSucursalResponse(

        @Schema(description = "ID de la sucursal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal",
                example = "Sucursal Norte")
        String nombreSucursal,

        @Schema(description = "Total facturado de ventas registradas",
                example = "15130.00")
        BigDecimal totalFacturado,

        @Schema(description = "Cantidad total de productos vendidos",
                example = "7")
        Long cantidadProductosVendidos

) {
}
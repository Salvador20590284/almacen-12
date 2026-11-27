package com.salvador.almacen.repositories;

import com.salvador.almacen.dto.Reportes.ReporteVentasSucursalResponse;
import com.salvador.almacen.entities.Venta;
import com.salvador.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByIdAndEstadoVenta(
            Long id, EstadoVenta estadoVenta);

    List<Venta> findAllByEstadoVenta(EstadoVenta estadoVenta);

    @Query("""
            SELECT new com.salvador.almacen.dto.Reportes.ReporteVentasSucursalResponse(
                s.id,
                s.nombre,
                COALESCE(SUM(d.cantidadProducto * d.precioProducto), 0),
                COALESCE(SUM(d.cantidadProducto), 0L)
            )
            FROM Sucursal s
            LEFT JOIN Venta v ON v.sucursal = s
                AND v.estadoVenta = :estadoVenta
            LEFT JOIN v.detalleVentas d
            GROUP BY s.id, s.nombre
            ORDER BY s.id
            """)
    List<ReporteVentasSucursalResponse> generarReportePorSucursal(
            @Param("estadoVenta") EstadoVenta estadoVenta);
}
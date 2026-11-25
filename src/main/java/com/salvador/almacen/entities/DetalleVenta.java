package com.salvador.almacen.entities;

import com.salvador.almacen.exceptions.DatoInvalidoException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DETALLES_VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @Column(name = "CANTIDAD_PRODUCTO", nullable = false)
    private Integer cantidadProducto;

    @Column(name = "PRECIO_PRODUCTO", precision = 10,
            scale = 2, nullable = false)
    private BigDecimal precioProducto;

    public void asignarVenta(Venta venta) {
        if(venta == null)
            throw new DatoInvalidoException("La venta es requerida");
        this.venta = venta;
    }

    //crear, actualizar etc..
}

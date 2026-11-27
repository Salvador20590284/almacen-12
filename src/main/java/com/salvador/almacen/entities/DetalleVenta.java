package com.salvador.almacen.entities;

import com.salvador.almacen.exceptions.DatoInvalidoException;
import com.salvador.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLES_VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
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


    public static void validarDatos(
            Producto producto, Integer cantidadProducto,
            BigDecimal precioProducto) {

        if (producto == null)
            throw new DatoInvalidoException(
                    "El producto es requerido");

        ValoresNumericosUtils.validarEnteroPositivo(
                cantidadProducto,
                "La cantidad del producto debe ser positiva");

        if (precioProducto == null ||
                precioProducto.compareTo(BigDecimal.ZERO) < 0)
            throw new DatoInvalidoException(
                    "El precio es requerido y no puede ser negativo");
    }


    public void asignarVenta(Venta venta) {

        if (venta == null)
            throw new DatoInvalidoException(
                    "La venta es requerida");

        this.venta = venta;
    }


    public static DetalleVenta crear(
            Venta venta, Producto producto,
            Integer cantidadProducto, BigDecimal precioProducto) {

        validarDatos(producto, cantidadProducto, precioProducto);

        DetalleVenta detalleVenta = new DetalleVenta();

        detalleVenta.asignarVenta(venta);
        detalleVenta.producto = producto;
        detalleVenta.cantidadProducto = cantidadProducto;
        detalleVenta.precioProducto = precioProducto;

        return detalleVenta;
    }


    public void actualizar(
            Producto producto, Integer cantidadProducto,
            BigDecimal precioProducto) {

        validarDatos(producto, cantidadProducto, precioProducto);

        this.producto = producto;
        this.cantidadProducto = cantidadProducto;
        this.precioProducto = precioProducto;
    }


    public BigDecimal calcularSubtotal() {

        return this.precioProducto.multiply(
                BigDecimal.valueOf(this.cantidadProducto));
    }
}
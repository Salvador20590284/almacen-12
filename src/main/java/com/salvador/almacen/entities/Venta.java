package com.salvador.almacen.entities;

import com.salvador.almacen.enums.EstadoVenta;
import com.salvador.almacen.exceptions.ConflictoException;
import com.salvador.almacen.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();


    public static void validarDatos(Sucursal sucursal) {

        if (sucursal == null)
            throw new DatoInvalidoException(
                    "La sucursal es requerida");
    }


    public static Venta crear(Sucursal sucursal) {

        validarDatos(sucursal);

        Venta venta = new Venta();

        venta.sucursal = sucursal;
        venta.fecha = LocalDate.now();
        venta.estadoVenta = EstadoVenta.REGISTRADA;

        return venta;
    }


    public void agregarDetalle(DetalleVenta detalleVenta) {

        if (detalleVenta == null)
            throw new DatoInvalidoException(
                    "El detalle de la venta es requerido");

        detalleVenta.asignarVenta(this);

        this.detalleVentas.add(detalleVenta);
    }


    public void cancelar() {

        if (this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictoException(
                    "La venta ya esta cancelada");

        this.estadoVenta = EstadoVenta.CANCELADA;
    }


    public BigDecimal calcularTotal() {

        return this.detalleVentas.stream()
                .map(DetalleVenta::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
package com.salvador.almacen.services.Ventas;

import com.salvador.almacen.dto.DetalleVenta.DetalleVentaRequest;
import com.salvador.almacen.dto.Ventas.VentaRequest;
import com.salvador.almacen.dto.Ventas.VentaResponse;
import com.salvador.almacen.entities.DetalleVenta;
import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.entities.Sucursal;
import com.salvador.almacen.entities.Venta;
import com.salvador.almacen.enums.EstadoVenta;
import com.salvador.almacen.exceptions.ConflictoException;
import com.salvador.almacen.exceptions.RecursoNoEncontradoException;
import com.salvador.almacen.mappers.VentaMapper;
import com.salvador.almacen.mappers.DetalleVentaMapper;
import com.salvador.almacen.repositories.ProductoRepository;
import com.salvador.almacen.repositories.SucursalRepository;
import com.salvador.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    private final VentaMapper ventaMapper;
    private final DetalleVentaMapper detalleVentaMapper;


    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {

        log.info("Listado de las ventas...");

        return ventaRepository
                .findAllByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {

        log.info("Listado de ventas canceladas...");

        return ventaRepository
                .findAllByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {

        return ventaMapper.entidadAResponse(
                obtenerVentaActivaOException(id));
    }


    @Override
    public VentaResponse registrar(VentaRequest request) {

        log.info("Registrando nueva venta...");
        Sucursal sucursal = obtenerSucursalOException(request.idSucursal());

        Venta venta = ventaMapper.requestAEntidad(request, sucursal);

        for (DetalleVentaRequest detalleRequest : request.productos()) {

            Producto producto = obtenerProductoOException(detalleRequest.idProducto());

            validarStock(producto, detalleRequest.cantidadProducto());

            DetalleVenta detalle = detalleVentaMapper.requestAEntidad(
                    detalleRequest, venta, producto);

            producto.descontarCantidad(
                    detalleRequest.cantidadProducto());

            venta.agregarDetalle(detalle);
        }

        ventaRepository.saveAndFlush(venta);

        log.info("Nueva venta con id: {} registrada", venta.getId());

        return ventaMapper.entidadAResponse(venta);
    }


    @Override
    public void cancelar(Long id) {

        log.info("Cancelando venta con id: {}", id);

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada con id: " + id));
        venta.cancelar();
        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(
                    detalle.getCantidadProducto());
        }
        ventaRepository.saveAndFlush(venta);
        log.info("Venta con id: {} cancelada", id);
    }


    private Venta obtenerVentaActivaOException(Long id) {

        log.info("Buscando venta activa con id: {}", id);

        return ventaRepository
                .findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Venta activa no encontrada con id: " + id));
    }


    private Sucursal obtenerSucursalOException(Long id) {

        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada con id: " + id));
    }


    private Producto obtenerProductoOException(Long id) {

        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con id: " + id));
    }


    private void validarStock(Producto producto, int cantidad) {

        log.info("Validando stock del producto: {}",
                producto.getNombre());

        if (producto.getCantidad() < cantidad)
            throw new ConflictoException(
                    "Stock insuficiente para el producto: "
                            + producto.getNombre());
    }
}
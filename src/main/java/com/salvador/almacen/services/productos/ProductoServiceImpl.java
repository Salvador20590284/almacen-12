package com.salvador.almacen.services.productos;

import com.salvador.almacen.dto.productos.ProductoRequest;
import com.salvador.almacen.dto.productos.ProductoResponse;
import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.enums.Categoria;
import com.salvador.almacen.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.salvador.almacen.mappers.ProductoMapper;
import org.springframework.stereotype.Service;
import com.salvador.almacen.repositories.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax) {

        log.info("Listado de los productos... ");
        return productoRepository.findAll().stream()
                //.map(p -> productoMapper.entidadResponse(p)).toList()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {

        log.info("Registrando nuevo producto... ");

        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()));

        productoRepository.saveAndFlush(producto);

        log.info("Nuevo producto {} registrado ", producto.getNombre());

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {

        Producto producto = obtenerProductoException(id);

        log.info("Actualizar producto con id: {}", id);

        producto.actualizar(
                request.nombre().trim(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad());

        productoRepository.saveAndFlush(producto);

        log.info("Producto con id: {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {

        Producto producto = obtenerProductoException(id);

        log.info("Eliminando producto con id: {}", id);

        productoRepository.delete(producto);
        productoRepository.flush();

        log.info("Producto con id {} eliminado", id);

    }

    private Producto obtenerProductoException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " +id));
    }
}
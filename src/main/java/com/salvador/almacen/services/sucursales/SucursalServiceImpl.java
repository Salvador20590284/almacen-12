package com.salvador.almacen.services.sucursales;


import com.salvador.almacen.dto.productos.ProductoRequest;
import com.salvador.almacen.dto.productos.ProductoResponse;
import com.salvador.almacen.dto.sucursales.SucursalRequest;
import com.salvador.almacen.dto.sucursales.SucursalResponse;
import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.entities.Sucursal;
import com.salvador.almacen.enums.Categoria;
import com.salvador.almacen.exceptions.ConflictoException;
import com.salvador.almacen.exceptions.RecursoNoEncontradoException;
import com.salvador.almacen.mappers.SucursalMapper;
import com.salvador.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class SucursalServiceImpl implements SucursalService{
    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listado de las sucursales...");

        return sucursalRepository.findAll()
                .stream()
                .map(sucursalMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenersucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {

        log.info("Registrando nueva sucursal... ");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        log.info("Nuevo sucursal registrado", sucursal.getNombre());

        return sucursalMapper.entidadAResponse(sucursal);
    }


    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {

        Sucursal sucursal = obtenersucursalOException(id);

        log.info("Actualizar sucursal con id: {}", id);

        validarCambiosUnicos(request, id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion());


        sucursalRepository.saveAndFlush(sucursal);

        log.info("Sucursal con id: {} actualizado", id);

        return sucursalMapper.entidadAResponse(sucursal);
    }




    @Override
    public void eliminar(Long id) {

        Sucursal sucursal = obtenersucursalOException(id);

        log.info("Eliminando sucursal con id: {}", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Sucursal con id {} eliminado", id);

    }

    private Sucursal obtenersucursalOException(Long id){

        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada con id: " + id));
    }


    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre unico...");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe una sucursal con el nombre de:"+ request.nombre());
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("Validando cambio nombre unico...");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(),id))
        throw new ConflictoException("Ya existe una sucursal con el nombre de:"+ request.nombre());
    }
}

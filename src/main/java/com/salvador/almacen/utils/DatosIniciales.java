package com.salvador.almacen.utils;

import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.enums.Categoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.salvador.almacen.repositories.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j


public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    @Override
    public void run(String... args) throws Exception {

        if(productoRepository.count()==0){
            productoRepository.saveAll(List.of(
               new Producto(
                       null,
                "Laptop Gamer",
            Categoria.ELECTRONICA,
                    BigDecimal.valueOf(15000),
                10),

            new Producto(
                    null,
                    "Mouse Inalambrico",
            Categoria.ELECTRONICA,
                    BigDecimal.valueOf(25),
            50),

            new Producto(
                    null,
                    "Camiseta Deportiva",
            Categoria.ROPA,
                    BigDecimal.valueOf(20),
            100)
            )
            );
        }
        log.info("Productos de prueba cargados correctamente");

    }
}

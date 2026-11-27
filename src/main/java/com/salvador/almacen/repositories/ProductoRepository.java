package com.salvador.almacen.repositories;

import com.salvador.almacen.entities.Producto;
import com.salvador.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Long> {

    @Query("""
    SELECT p FROM Producto p
    WHERE LOCATE(LOWER(COALESCE(:nombre, p.nombre)), LOWER(p.nombre)) > 0
    AND p.categoria = COALESCE(:categoria, p.categoria)
    AND p.precio BETWEEN COALESCE(:precioMin, p.precio)
                     AND COALESCE(:precioMax, p.precio)
    """)
    List<Producto> listarConFiltros(
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax);
}

package com.salvador.almacen.repositories;

import com.salvador.almacen.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Long> {
}

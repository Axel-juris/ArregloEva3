package com.duoc.radar.oportunidades.repository;

import com.duoc.radar.oportunidades.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Integer> {
    List<Seguimiento> findByOportunidadIdOrderByIdDesc(Integer oportunidadId);
    void deleteByOportunidadId(Integer oportunidadId);
}

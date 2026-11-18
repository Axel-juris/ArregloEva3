package com.duoc.radar.oportunidades.repository;

import com.duoc.radar.oportunidades.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByOrganizacionId(Integer organizacionId);
    void deleteByOrganizacionId(Integer organizacionId);
}

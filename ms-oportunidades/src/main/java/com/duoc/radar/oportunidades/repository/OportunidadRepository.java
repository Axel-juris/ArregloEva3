package com.duoc.radar.oportunidades.repository;

import com.duoc.radar.oportunidades.model.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OportunidadRepository extends JpaRepository<Oportunidad, Integer> {
    List<Oportunidad> findByEjecutivoId(Integer ejecutivoId);
    List<Oportunidad> findByOrganizacionId(Integer organizacionId);
    List<Oportunidad> findByContactoId(Integer contactoId);
}

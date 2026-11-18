package com.duoc.radar.oportunidades.controller;

import com.duoc.radar.oportunidades.model.Oportunidad;
import com.duoc.radar.oportunidades.model.Organizacion;
import com.duoc.radar.oportunidades.repository.ContactoRepository;
import com.duoc.radar.oportunidades.repository.OportunidadRepository;
import com.duoc.radar.oportunidades.repository.OrganizacionRepository;
import com.duoc.radar.oportunidades.repository.SeguimientoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    private final OrganizacionRepository repo;
    private final ContactoRepository contactoRepo;
    private final OportunidadRepository oportunidadRepo;
    private final SeguimientoRepository seguimientoRepo;

    public OrganizacionController(OrganizacionRepository repo,
                                  ContactoRepository contactoRepo,
                                  OportunidadRepository oportunidadRepo,
                                  SeguimientoRepository seguimientoRepo) {
        this.repo = repo;
        this.contactoRepo = contactoRepo;
        this.oportunidadRepo = oportunidadRepo;
        this.seguimientoRepo = seguimientoRepo;
    }

    @GetMapping
    public List<Organizacion> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Organizacion obtener(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> noEncontrada(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organizacion crear(@Valid @RequestBody Organizacion organizacion) {
        organizacion.setId(null);
        return repo.save(organizacion);
    }

    @PutMapping("/{id}")
    public Organizacion actualizar(@PathVariable Integer id, @Valid @RequestBody Organizacion datos) {
        Organizacion existente = repo.findById(id).orElseThrow(() -> noEncontrada(id));
        BeanUtils.copyProperties(datos, existente, "id");
        return repo.save(existente);
    }

    // Borrado en cascada: seguimientos -> oportunidades -> contactos -> organización
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw noEncontrada(id);
        }
        List<Oportunidad> oportunidades = oportunidadRepo.findByOrganizacionId(id);
        oportunidades.forEach(o -> seguimientoRepo.deleteByOportunidadId(o.getId()));
        oportunidadRepo.deleteAll(oportunidades);
        contactoRepo.deleteByOrganizacionId(id);
        repo.deleteById(id);
    }

    private ResponseStatusException noEncontrada(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Organización " + id + " no encontrada");
    }
}

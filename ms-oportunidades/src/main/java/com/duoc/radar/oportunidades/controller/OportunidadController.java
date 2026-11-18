package com.duoc.radar.oportunidades.controller;

import com.duoc.radar.oportunidades.dto.AsignacionRequest;
import com.duoc.radar.oportunidades.dto.EstadoRequest;
import com.duoc.radar.oportunidades.model.Oportunidad;
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
@RequestMapping("/api/oportunidades")
public class OportunidadController {

    private final OportunidadRepository repo;
    private final OrganizacionRepository organizacionRepo;
    private final ContactoRepository contactoRepo;
    private final SeguimientoRepository seguimientoRepo;

    public OportunidadController(OportunidadRepository repo,
                                 OrganizacionRepository organizacionRepo,
                                 ContactoRepository contactoRepo,
                                 SeguimientoRepository seguimientoRepo) {
        this.repo = repo;
        this.organizacionRepo = organizacionRepo;
        this.contactoRepo = contactoRepo;
        this.seguimientoRepo = seguimientoRepo;
    }

    // GET /api/oportunidades                 -> todas (supervisor)
    // GET /api/oportunidades?ejecutivoId=2   -> solo las de un ejecutivo
    @GetMapping
    public List<Oportunidad> listar(@RequestParam(required = false) Integer ejecutivoId) {
        return ejecutivoId == null ? repo.findAll() : repo.findByEjecutivoId(ejecutivoId);
    }

    @GetMapping("/{id}")
    public Oportunidad obtener(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> noEncontrada(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Oportunidad crear(@Valid @RequestBody Oportunidad oportunidad) {
        verificarReferencias(oportunidad);
        oportunidad.setId(null);
        return repo.save(oportunidad);
    }

    @PutMapping("/{id}")
    public Oportunidad actualizar(@PathVariable Integer id, @Valid @RequestBody Oportunidad datos) {
        Oportunidad existente = repo.findById(id).orElseThrow(() -> noEncontrada(id));
        verificarReferencias(datos);
        BeanUtils.copyProperties(datos, existente, "id");
        return repo.save(existente);
    }

    // PATCH /api/oportunidades/5/estado   {"estado":"EN_GESTION"}
    @PatchMapping("/{id}/estado")
    public Oportunidad cambiarEstado(@PathVariable Integer id, @Valid @RequestBody EstadoRequest request) {
        Oportunidad existente = repo.findById(id).orElseThrow(() -> noEncontrada(id));
        existente.setEstado(request.estado());
        return repo.save(existente);
    }

    // PATCH /api/oportunidades/5/ejecutivo   {"ejecutivoId":2}
    @PatchMapping("/{id}/ejecutivo")
    public Oportunidad reasignar(@PathVariable Integer id, @Valid @RequestBody AsignacionRequest request) {
        Oportunidad existente = repo.findById(id).orElseThrow(() -> noEncontrada(id));
        existente.setEjecutivoId(request.ejecutivoId());
        return repo.save(existente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw noEncontrada(id);
        }
        seguimientoRepo.deleteByOportunidadId(id);
        repo.deleteById(id);
    }

    private void verificarReferencias(Oportunidad o) {
        if (!organizacionRepo.existsById(o.getOrganizacionId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La organización " + o.getOrganizacionId() + " no existe");
        }
        if (o.getContactoId() != null && !contactoRepo.existsById(o.getContactoId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El contacto " + o.getContactoId() + " no existe");
        }
    }

    private ResponseStatusException noEncontrada(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidad " + id + " no encontrada");
    }
}

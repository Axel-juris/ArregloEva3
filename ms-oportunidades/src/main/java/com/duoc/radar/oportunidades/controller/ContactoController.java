package com.duoc.radar.oportunidades.controller;

import com.duoc.radar.oportunidades.model.Contacto;
import com.duoc.radar.oportunidades.model.Oportunidad;
import com.duoc.radar.oportunidades.repository.ContactoRepository;
import com.duoc.radar.oportunidades.repository.OportunidadRepository;
import com.duoc.radar.oportunidades.repository.OrganizacionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoRepository repo;
    private final OrganizacionRepository organizacionRepo;
    private final OportunidadRepository oportunidadRepo;

    public ContactoController(ContactoRepository repo,
                              OrganizacionRepository organizacionRepo,
                              OportunidadRepository oportunidadRepo) {
        this.repo = repo;
        this.organizacionRepo = organizacionRepo;
        this.oportunidadRepo = oportunidadRepo;
    }

    // GET /api/contactos                      -> todos
    // GET /api/contactos?organizacionId=3     -> los de una organización
    @GetMapping
    public List<Contacto> listar(@RequestParam(required = false) Integer organizacionId) {
        return organizacionId == null ? repo.findAll() : repo.findByOrganizacionId(organizacionId);
    }

    @GetMapping("/{id}")
    public Contacto obtener(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contacto crear(@Valid @RequestBody Contacto contacto) {
        verificarOrganizacion(contacto.getOrganizacionId());
        contacto.setId(null);
        return repo.save(contacto);
    }

    @PutMapping("/{id}")
    public Contacto actualizar(@PathVariable Integer id, @Valid @RequestBody Contacto datos) {
        Contacto existente = repo.findById(id).orElseThrow(() -> noEncontrado(id));
        verificarOrganizacion(datos.getOrganizacionId());
        BeanUtils.copyProperties(datos, existente, "id");
        return repo.save(existente);
    }

    // Al borrar un contacto, sus oportunidades quedan sin contacto (no se borran)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw noEncontrado(id);
        }
        List<Oportunidad> relacionadas = oportunidadRepo.findByContactoId(id);
        relacionadas.forEach(o -> o.setContactoId(null));
        oportunidadRepo.saveAll(relacionadas);
        repo.deleteById(id);
    }

    private void verificarOrganizacion(Integer organizacionId) {
        if (!organizacionRepo.existsById(organizacionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La organización " + organizacionId + " no existe");
        }
    }

    private ResponseStatusException noEncontrado(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto " + id + " no encontrado");
    }
}

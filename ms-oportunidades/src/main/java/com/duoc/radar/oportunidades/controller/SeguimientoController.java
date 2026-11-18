package com.duoc.radar.oportunidades.controller;

import com.duoc.radar.oportunidades.model.Seguimiento;
import com.duoc.radar.oportunidades.repository.OportunidadRepository;
import com.duoc.radar.oportunidades.repository.SeguimientoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
public class SeguimientoController {

    private final SeguimientoRepository repo;
    private final OportunidadRepository oportunidadRepo;

    public SeguimientoController(SeguimientoRepository repo, OportunidadRepository oportunidadRepo) {
        this.repo = repo;
        this.oportunidadRepo = oportunidadRepo;
    }

    // GET /api/seguimientos                    -> todos
    // GET /api/seguimientos?oportunidadId=5    -> historial de una oportunidad (el más reciente primero)
    @GetMapping
    public List<Seguimiento> listar(@RequestParam(required = false) Integer oportunidadId) {
        return oportunidadId == null ? repo.findAll() : repo.findByOportunidadIdOrderByIdDesc(oportunidadId);
    }

    @GetMapping("/{id}")
    public Seguimiento obtener(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Seguimiento crear(@Valid @RequestBody Seguimiento seguimiento) {
        verificarOportunidad(seguimiento.getOportunidadId());
        seguimiento.setId(null);
        return repo.save(seguimiento);
    }

    @PutMapping("/{id}")
    public Seguimiento actualizar(@PathVariable Integer id, @Valid @RequestBody Seguimiento datos) {
        Seguimiento existente = repo.findById(id).orElseThrow(() -> noEncontrado(id));
        verificarOportunidad(datos.getOportunidadId());
        BeanUtils.copyProperties(datos, existente, "id");
        return repo.save(existente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw noEncontrado(id);
        }
        repo.deleteById(id);
    }

    private void verificarOportunidad(Integer oportunidadId) {
        if (!oportunidadRepo.existsById(oportunidadId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La oportunidad " + oportunidadId + " no existe");
        }
    }

    private ResponseStatusException noEncontrado(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguimiento " + id + " no encontrado");
    }
}

package com.duoc.radar.usuarios.controller;

import com.duoc.radar.usuarios.model.Usuario;
import com.duoc.radar.usuarios.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/ejecutivos")
    public List<Usuario> ejecutivos() {
        return repo.findByRol("EJECUTIVO");
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@Valid @RequestBody Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave es obligatoria");
        }
        usuario.setId(null); // el id lo genera la base de datos
        return repo.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Integer id, @Valid @RequestBody Usuario datos) {
        Usuario existente = repo.findById(id).orElseThrow(() -> noEncontrado(id));
        existente.setNombre(datos.getNombre());
        existente.setCorreo(datos.getCorreo());
        existente.setRol(datos.getRol());
        // Si no se envía clave, se conserva la actual
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            existente.setPassword(datos.getPassword());
        }
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

    private ResponseStatusException noEncontrado(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario " + id + " no encontrado");
    }
}

package com.duoc.radar.usuarios.controller;

import com.duoc.radar.usuarios.dto.LoginRequest;
import com.duoc.radar.usuarios.model.Usuario;
import com.duoc.radar.usuarios.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository repo;

    public AuthController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/login")
    public Usuario login(@Valid @RequestBody LoginRequest request) {
        return repo.findByCorreoAndPassword(request.correo().trim(), request.password())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Correo o clave incorrectos"));
    }
}

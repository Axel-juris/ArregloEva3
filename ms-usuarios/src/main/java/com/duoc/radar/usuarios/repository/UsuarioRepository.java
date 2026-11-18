package com.duoc.radar.usuarios.repository;

import com.duoc.radar.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);
    List<Usuario> findByRol(String rol);
}

package com.duoc.radar.usuarios.config;

import com.duoc.radar.usuarios.model.Usuario;
import com.duoc.radar.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DatosIniciales {

    // Cuentas ficticias: las mismas que usaba la app con Room
    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                        new Usuario(null, "Carlos Supervisor", "supervisor@duoc.cl", "clave123", "SUPERVISOR"),
                        new Usuario(null, "Ana Ejecutivo", "ana@duoc.cl", "clave123", "EJECUTIVO"),
                        new Usuario(null, "Bruno Ejecutivo", "bruno@duoc.cl", "clave123", "EJECUTIVO"),
                        new Usuario(null, "Carla Ejecutivo", "carla@duoc.cl", "clave123", "EJECUTIVO")
                ));
            }
        };
    }
}

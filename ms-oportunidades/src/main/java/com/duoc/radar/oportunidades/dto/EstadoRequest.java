package com.duoc.radar.oportunidades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstadoRequest(
        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "NUEVA|EN_GESTION|CONFIRMADA|DESCARTADA", message = "Estado no válido")
        String estado
) {}

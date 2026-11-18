package com.duoc.radar.oportunidades.dto;

import jakarta.validation.constraints.NotNull;

public record AsignacionRequest(
        @NotNull(message = "El ejecutivo es obligatorio") Integer ejecutivoId
) {}

package com.duoc.radar.oportunidades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seguimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La oportunidad es obligatoria")
    private Integer oportunidadId;

    @NotNull(message = "El ejecutivo es obligatorio")
    private Integer ejecutivoId;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "El tipo de gestión es obligatorio")
    @Pattern(regexp = "LLAMADA|REUNION|CORREO|VISITA", message = "Tipo de gestión no válido")
    private String tipoGestion;

    @NotBlank(message = "La observación es obligatoria")
    @Column(length = 1000)
    private String observacion;

    private String proximaGestionFecha;
}

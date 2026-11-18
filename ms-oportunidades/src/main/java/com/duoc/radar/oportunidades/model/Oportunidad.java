package com.duoc.radar.oportunidades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oportunidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Column(length = 1000)
    private String descripcion = "";

    @PositiveOrZero(message = "El monto no puede ser negativo")
    private Double monto = 0.0;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "NUEVA|EN_GESTION|CONFIRMADA|DESCARTADA", message = "Estado no válido")
    private String estado;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "ALTA|MEDIA|BAJA", message = "Prioridad no válida")
    private String prioridad;

    @NotBlank(message = "La fecha de creación es obligatoria")
    private String fechaCreacion;

    @NotBlank(message = "La fecha del evento es obligatoria")
    private String fechaEvento;

    private String fechaTermino = "";

    @NotBlank(message = "El horario es obligatorio")
    private String horario;

    @NotBlank(message = "El lugar es obligatorio")
    private String lugarEvento;

    @NotBlank(message = "El tipo de evento es obligatorio")
    private String tipoEvento;

    @Min(value = 0, message = "El público esperado no puede ser negativo")
    private Integer publicoEsperado = 0;

    @NotNull(message = "La organización es obligatoria")
    private Integer organizacionId;

    private Integer contactoId;   // opcional

    private Integer ejecutivoId;  // responsable (vive en ms-usuarios, por eso no es una FK)

    private String fotoUri;
}

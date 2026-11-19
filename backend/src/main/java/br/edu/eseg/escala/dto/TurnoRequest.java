package br.edu.eseg.escala.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/** Corpo do POST /api/turnos. Horários no formato HH:MM. */
public record TurnoRequest(
        @NotBlank(message = "é obrigatório") String nome,
        @NotNull(message = "é obrigatório")
        @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "use o formato HH:MM")
        String horaInicio,
        @NotNull(message = "é obrigatório")
        @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "use o formato HH:MM")
        String horaFim,
        @NotNull(message = "é obrigatório") @Min(value = 1, message = "deve ser maior que zero") Integer duracaoHoras) {
}

package br.edu.eseg.escala.dto;

import jakarta.validation.constraints.NotBlank;

/** Corpo do POST /api/predios. */
public record PredioRequest(
        @NotBlank(message = "é obrigatório") String nome,
        String endereco) {
}

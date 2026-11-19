package br.edu.eseg.escala.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Corpo do POST /api/departamentos.
 * idsEspecialidades: especialidades que o departamento aceita (tabela departamento_especialidade).
 */
public record DepartamentoRequest(
        @NotBlank(message = "é obrigatório") String nome,
        @NotNull(message = "é obrigatório") Long idPredio,
        @NotEmpty(message = "informe ao menos uma especialidade") List<Long> idsEspecialidades) {
}

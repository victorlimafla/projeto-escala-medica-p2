package br.edu.eseg.escala.dto.painel;

/** R1: horas de escala por especialidade. */
public record EspecialidadeResumoResponse(
        String especialidade,
        Long medicos,
        Long medicosEscalados,
        Long horasAlocadas) {
}

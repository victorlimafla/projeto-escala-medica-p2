package br.edu.eseg.escala.dto.painel;

/** R5: médicos com plantões noturnos na semana. */
public record PlantonistaNoturnoResponse(String medico, String especialidade, Long noites) {
}

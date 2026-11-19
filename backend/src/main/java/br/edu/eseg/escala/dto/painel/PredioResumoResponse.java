package br.edu.eseg.escala.dto.painel;

/** R2: movimento de cada prédio na semana. */
public record PredioResumoResponse(
        String predio,
        Long departamentos,
        Long plantoes,
        Long medicos,
        Long horasAlocadas) {
}

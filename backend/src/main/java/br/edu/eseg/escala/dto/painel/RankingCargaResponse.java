package br.edu.eseg.escala.dto.painel;

/** R3: médicos que mais usam a carga do regime. */
public record RankingCargaResponse(
        String medico,
        String especialidade,
        String regime,
        Long horasAlocadas,
        Integer cargaSemanal,
        Double percentualCarga) {

    /** Atalho: recebe horas e carga e calcula o percentual com 1 casa decimal. */
    public RankingCargaResponse(String medico, String especialidade, String regime, Long horasAlocadas,
                                Integer cargaSemanal) {
        this(medico, especialidade, regime, horasAlocadas, cargaSemanal,
                Math.round(horasAlocadas * 1000.0 / cargaSemanal) / 10.0);
    }
}

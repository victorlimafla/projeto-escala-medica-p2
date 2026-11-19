package br.edu.eseg.escala.dto.painel;

import br.edu.eseg.escala.model.DiaSemana;

/** R6: o retrato de cada dia da semana. */
public record ResumoDiaResponse(
        Integer diaSemana,
        String dia,
        Long medicos,
        Long departamentos,
        Long horasAlocadas) {

    /** Atalho: recebe o número do dia e preenche o nome (1 = Segunda-feira ... 7 = Domingo). */
    public ResumoDiaResponse(Integer diaSemana, Long medicos, Long departamentos, Long horasAlocadas) {
        this(diaSemana, DiaSemana.de(diaSemana).getNome(), medicos, departamentos, horasAlocadas);
    }
}

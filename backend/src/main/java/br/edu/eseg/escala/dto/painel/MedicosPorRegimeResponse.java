package br.edu.eseg.escala.dto.painel;

/** R0 (exemplo resolvido): quantos médicos há em cada regime de trabalho. */
public record MedicosPorRegimeResponse(String regime, Integer cargaSemanal, Long medicos) {
}

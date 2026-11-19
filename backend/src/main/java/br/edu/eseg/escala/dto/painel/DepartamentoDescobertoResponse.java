package br.edu.eseg.escala.dto.painel;

/** R4: departamento sem nenhum médico escalado no dia pedido. */
public record DepartamentoDescobertoResponse(Long idDepartamento, String departamento, String predio) {
}

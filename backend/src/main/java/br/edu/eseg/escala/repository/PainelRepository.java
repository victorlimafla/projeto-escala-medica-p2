package br.edu.eseg.escala.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import br.edu.eseg.escala.dto.painel.MedicosPorRegimeResponse;
import br.edu.eseg.escala.model.Alocacao;

/**
 * Consultas do Painel (Avaliação P2).
 *
 * Só o exemplo R0 está pronto. Escreva aqui um método com @Query para cada relatório (R1 a R6)
 * e depois chame-o no PainelService. Pode usar JPQL ou SQL nativo (nativeQuery = true).
 *
 * Repository<Alocacao, Long>: o Spring cria a implementação; Alocacao é só a entidade "dona".
 */
public interface PainelRepository extends Repository<Alocacao, Long> {

    /**
     * R0 (EXEMPLO RESOLVIDO). Use como modelo.
     * JPQL: trabalha com entidades e atributos (Medico, m.regime), não com tabelas e colunas.
     * "select new" monta o record de resposta direto na consulta.
     */
    @Query("""
            select new br.edu.eseg.escala.dto.painel.MedicosPorRegimeResponse(
                   r.descricao, r.cargaHorariaSemanal, count(m.id))
            from Medico m
            join m.regime r
            group by r.id, r.descricao, r.cargaHorariaSemanal
            order by r.cargaHorariaSemanal, r.descricao
            """)
    List<MedicosPorRegimeResponse> medicosPorRegime();
}

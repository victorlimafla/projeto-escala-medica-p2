
package br.edu.eseg.escala.repository;

import java.util.List;

import br.edu.eseg.escala.dto.painel.*;
import br.edu.eseg.escala.model.Alocacao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Consultas do Painel (Avaliação P2).

 * Só o exemplo R0 está pronto. Escreva aqui um método com @Query para cada relatório (R1 a R6)
 * e depois chame-o no PainelService. Pode usar JPQL ou SQL nativo (nativeQuery = true).

 * Repository<Alocacao, Long>: o Spring cria a implementação; Alocacao é só a entidade "dona".
 */
public interface PainelRepository extends Repository<Alocacao, Long> {

    /**
     * R0 - Médicos por regime.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.MedicosPorRegimeResponse(
                  r.descricao,
                  r.cargaHorariaSemanal,
                  count(m.id))
           from Medico m
           join m.regime r
           group by r.id, r.descricao, r.cargaHorariaSemanal
           order by r.cargaHorariaSemanal, r.descricao
           """)
    List<MedicosPorRegimeResponse> medicosPorRegime();


    /**
     * R1 - Resumo por especialidade.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.EspecialidadeResumoResponse(
                  e.nome,
                  count(distinct m.id),
                  count(distinct a.medico.id),
                  coalesce(sum(t.duracaoHoras), 0))
           from Especialidade e
           left join Medico m on m.especialidade = e
           left join Alocacao a on a.medico = m
           left join a.turno t
           group by e.id, e.nome
           order by coalesce(sum(t.duracaoHoras), 0) desc, e.nome
           """)
    List<EspecialidadeResumoResponse> especialidades();
}
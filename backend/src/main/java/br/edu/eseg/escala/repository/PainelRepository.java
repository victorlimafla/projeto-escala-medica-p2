
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


    /**
     * R2 - Movimento por prédio.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.PredioResumoResponse(
                  p.nome,
                  count(distinct d.id),
                  count(a.id),
                  count(distinct a.medico.id),
                  coalesce(sum(t.duracaoHoras), 0))
           from Predio p
           join Departamento d on d.predio = p
           left join Alocacao a on a.departamento = d
           left join a.turno t
           group by p.id, p.nome
           order by p.nome
           """)
    List<PredioResumoResponse> predios();


    /**
     * R3 - Ranking de utilização da carga horária.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.RankingCargaResponse(
                  m.nome,
                  e.nome,
                  r.descricao,
                  sum(t.duracaoHoras),
                  r.cargaHorariaSemanal)
           from Medico m
           join m.especialidade e
           join m.regime r
           join Alocacao a on a.medico = m
           join a.turno t
           group by m.id, m.nome, e.nome,
                    r.descricao, r.cargaHorariaSemanal
           order by
                  sum(t.duracaoHoras) * 1.0 /
                      r.cargaHorariaSemanal desc,
                  sum(t.duracaoHoras) desc,
                  m.nome
           limit :limite
           """)
    List<RankingCargaResponse> rankingCarga(
            @Param("limite") Integer limite);


    /**
     * R4 - Departamentos sem alocações no dia informado.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.DepartamentoDescobertoResponse(
                  d.id,
                  d.nome,
                  p.nome)
           from Departamento d
           join d.predio p
           where not exists (
               select 1
               from Alocacao a
               where a.departamento = d
                 and a.diaSemana = :dia
           )
           order by p.nome, d.nome
           """)
    List<DepartamentoDescobertoResponse> departamentosDescobertos(
            @Param("dia") Integer dia);


    /**
     * R5 - Médicos com pelo menos N plantões noturnos.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.PlantonistaNoturnoResponse(
                  m.nome,
                  e.nome,
                  count(a.id))
           from Medico m
           join m.especialidade e
           join Alocacao a on a.medico = m
           join a.turno t
           where t.nome = 'Noite'
           group by m.id, m.nome, e.nome
           having count(a.id) >= :minimo
           order by count(a.id) desc, m.nome
           """)
    List<PlantonistaNoturnoResponse> plantonistasNoturnos(
            @Param("minimo") Integer minimo);


    /**
     * R6 - Resumo das alocações por dia da semana.
     */
    @Query("""
           select new br.edu.eseg.escala.dto.painel.ResumoDiaResponse(
                  a.diaSemana,
                  count(distinct a.medico.id),
                  count(distinct a.departamento.id),
                  coalesce(sum(t.duracaoHoras), 0))
           from Alocacao a
           join a.turno t
           group by a.diaSemana
           order by a.diaSemana
           """)
    List<ResumoDiaResponse> dias();
}

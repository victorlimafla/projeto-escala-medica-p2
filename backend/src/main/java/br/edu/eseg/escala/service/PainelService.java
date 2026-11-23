package br.edu.eseg.escala.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.eseg.escala.dto.painel.DepartamentoDescobertoResponse;
import br.edu.eseg.escala.dto.painel.EspecialidadeResumoResponse;
import br.edu.eseg.escala.dto.painel.MedicosPorRegimeResponse;
import br.edu.eseg.escala.dto.painel.PlantonistaNoturnoResponse;
import br.edu.eseg.escala.dto.painel.PredioResumoResponse;
import br.edu.eseg.escala.dto.painel.RankingCargaResponse;
import br.edu.eseg.escala.dto.painel.ResumoDiaResponse;
import br.edu.eseg.escala.exception.NaoImplementadoException;
import br.edu.eseg.escala.repository.PainelRepository;

/**
 * Painel de Gestão da Escala (Avaliação P2).
 * Cada método corresponde a um relatório pedido pela equipe de frontend.
 */
@Service
@Transactional(readOnly = true)
public class PainelService {

    private final PainelRepository painel;

    public PainelService(PainelRepository painel) {
        this.painel = painel;
    }

    /** R0: exemplo resolvido. */
    public List<MedicosPorRegimeResponse> medicosPorRegime() {
        return painel.medicosPorRegime();
    }

    /** R1: horas de escala por especialidade. */
    public List<EspecialidadeResumoResponse> especialidades() {
        // TODO R1: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        return painel.especialidades();
    }

    /** R2: movimento de cada prédio. */
    public List<PredioResumoResponse> predios() {
        // TODO R2: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        return painel.predios();
    }

    /** R3: ranking de uso da carga. limite = quantas linhas devolver. */
    public List<RankingCargaResponse> rankingCarga(Integer limite) {
        // TODO R3: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        return painel.rankingCarga(limite);
    }

    /** R4: departamentos sem ninguém no dia (1 = segunda ... 7 = domingo). */
    public List<DepartamentoDescobertoResponse> departamentosDescobertos(Integer dia) {
        // TODO R4: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        return painel.departamentosDescobertos(dia);
    }

    /** R5: médicos com pelo menos "minimo" plantões noturnos. */
    public List<PlantonistaNoturnoResponse> plantonistasNoturnos(Integer minimo) {
        // TODO R5: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        return painel.plantonistasNoturnos(minimo);
    }

    /** R6: resumo de cada dia da semana. */
    public List<ResumoDiaResponse> dias() {
        // TODO R6: crie o método no PainelRepository e troque a linha abaixo por return painel.<metodo>(...);
        throw new NaoImplementadoException("R6");
    }
}

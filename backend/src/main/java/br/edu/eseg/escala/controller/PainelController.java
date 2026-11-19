package br.edu.eseg.escala.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.eseg.escala.dto.painel.DepartamentoDescobertoResponse;
import br.edu.eseg.escala.dto.painel.EspecialidadeResumoResponse;
import br.edu.eseg.escala.dto.painel.MedicosPorRegimeResponse;
import br.edu.eseg.escala.dto.painel.PlantonistaNoturnoResponse;
import br.edu.eseg.escala.dto.painel.PredioResumoResponse;
import br.edu.eseg.escala.dto.painel.RankingCargaResponse;
import br.edu.eseg.escala.dto.painel.ResumoDiaResponse;
import br.edu.eseg.escala.service.PainelService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Painel de Gestão da Escala (Avaliação P2).
 * Este controller está PRONTO: não precisa ser alterado.
 */
@RestController
@RequestMapping("/api/painel")
public class PainelController {

    private final PainelService service;

    public PainelController(PainelService service) {
        this.service = service;
    }

    /** R0 (exemplo resolvido) */
    @GetMapping("/exemplo/medicos-por-regime")
    public List<MedicosPorRegimeResponse> medicosPorRegime() {
        return service.medicosPorRegime();
    }

    /** R1 */
    @GetMapping("/especialidades")
    public List<EspecialidadeResumoResponse> especialidades() {
        return service.especialidades();
    }

    /** R2 */
    @GetMapping("/predios")
    public List<PredioResumoResponse> predios() {
        return service.predios();
    }

    /** R3  GET /api/painel/ranking-carga?limite=5 */
    @GetMapping("/ranking-carga")
    public List<RankingCargaResponse> rankingCarga(
            @RequestParam(defaultValue = "5") @Min(value = 1, message = "deve ser de 1 a 36")
            @Max(value = 36, message = "deve ser de 1 a 36") Integer limite) {
        return service.rankingCarga(limite);
    }

    /** R4  GET /api/painel/departamentos-descobertos?dia=7 */
    @GetMapping("/departamentos-descobertos")
    public List<DepartamentoDescobertoResponse> departamentosDescobertos(
            @RequestParam @Min(value = 1, message = "deve ser de 1 a 7") @Max(value = 7, message = "deve ser de 1 a 7")
            Integer dia) {
        return service.departamentosDescobertos(dia);
    }

    /** R5  GET /api/painel/plantonistas-noturnos?minimo=2 */
    @GetMapping("/plantonistas-noturnos")
    public List<PlantonistaNoturnoResponse> plantonistasNoturnos(
            @RequestParam(defaultValue = "2") @Min(value = 1, message = "deve ser maior que zero") Integer minimo) {
        return service.plantonistasNoturnos(minimo);
    }

    /** R6 */
    @GetMapping("/dias")
    public List<ResumoDiaResponse> dias() {
        return service.dias();
    }
}

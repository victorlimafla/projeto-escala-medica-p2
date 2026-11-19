package br.edu.eseg.escala.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.eseg.escala.dto.DepartamentoRequest;
import br.edu.eseg.escala.dto.DepartamentoResponse;
import br.edu.eseg.escala.dto.EspecialidadeResponse;
import br.edu.eseg.escala.dto.PredioRequest;
import br.edu.eseg.escala.dto.PredioResponse;
import br.edu.eseg.escala.dto.RegimeResponse;
import br.edu.eseg.escala.dto.TurnoRequest;
import br.edu.eseg.escala.dto.TurnoResponse;
import br.edu.eseg.escala.service.CadastroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CadastroController {

    private final CadastroService service;

    public CadastroController(CadastroService service) {
        this.service = service;
    }

    @GetMapping("/predios")
    public List<PredioResponse> predios() {
        return service.predios();
    }

    /** GET /api/departamentos  ou  /api/departamentos?idPredio=2 */
    @GetMapping("/departamentos")
    public List<DepartamentoResponse> departamentos(@RequestParam(required = false) Long idPredio) {
        return service.departamentos(idPredio);
    }

    @GetMapping("/departamentos/{id}")
    public DepartamentoResponse departamento(@PathVariable Long id) {
        return service.departamento(id);
    }

    @GetMapping("/turnos")
    public List<TurnoResponse> turnos() {
        return service.turnos();
    }

    @GetMapping("/especialidades")
    public List<EspecialidadeResponse> especialidades() {
        return service.especialidades();
    }

    @GetMapping("/regimes")
    public List<RegimeResponse> regimes() {
        return service.regimes();
    }

    /** POST /api/predios  {"nome": "Bloco A", "endereco": "Emergência"} */
    @PostMapping("/predios")
    public ResponseEntity<PredioResponse> criarPredio(@Valid @RequestBody PredioRequest req) {
        PredioResponse novo = service.criarPredio(req);
        return ResponseEntity.created(URI.create("/api/predios/" + novo.id())).body(novo);
    }

    /** POST /api/departamentos  {"nome": "Pronto-Socorro Adulto", "idPredio": 1, "idsEspecialidades": [1, 4]} */
    @PostMapping("/departamentos")
    public ResponseEntity<DepartamentoResponse> criarDepartamento(@Valid @RequestBody DepartamentoRequest req) {
        DepartamentoResponse novo = service.criarDepartamento(req);
        return ResponseEntity.created(URI.create("/api/departamentos/" + novo.id())).body(novo);
    }

    /** POST /api/turnos  {"nome": "Manhã", "horaInicio": "07:00", "horaFim": "13:00", "duracaoHoras": 6} */
    @PostMapping("/turnos")
    public ResponseEntity<TurnoResponse> criarTurno(@Valid @RequestBody TurnoRequest req) {
        TurnoResponse novo = service.criarTurno(req);
        return ResponseEntity.created(URI.create("/api/turnos/" + novo.id())).body(novo);
    }
}

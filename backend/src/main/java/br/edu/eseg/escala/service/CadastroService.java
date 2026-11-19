package br.edu.eseg.escala.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.eseg.escala.dto.DepartamentoRequest;
import br.edu.eseg.escala.dto.DepartamentoResponse;
import br.edu.eseg.escala.dto.EspecialidadeResponse;
import br.edu.eseg.escala.dto.PredioRequest;
import br.edu.eseg.escala.dto.PredioResponse;
import br.edu.eseg.escala.dto.RegimeResponse;
import br.edu.eseg.escala.dto.TurnoRequest;
import br.edu.eseg.escala.dto.TurnoResponse;
import br.edu.eseg.escala.exception.RecursoNaoEncontradoException;
import br.edu.eseg.escala.exception.RegraNegocioException;
import br.edu.eseg.escala.model.Departamento;
import br.edu.eseg.escala.model.Especialidade;
import br.edu.eseg.escala.model.Predio;
import br.edu.eseg.escala.model.Turno;
import br.edu.eseg.escala.repository.DepartamentoRepository;
import br.edu.eseg.escala.repository.EspecialidadeRepository;
import br.edu.eseg.escala.repository.PredioRepository;
import br.edu.eseg.escala.repository.RegimeTrabalhoRepository;
import br.edu.eseg.escala.repository.TurnoRepository;

/** Tabelas de apoio: consulta de todas e cadastro de prédios, departamentos e turnos. */
@Service
@Transactional(readOnly = true)
public class CadastroService {

    private final PredioRepository predios;
    private final DepartamentoRepository departamentos;
    private final TurnoRepository turnos;
    private final EspecialidadeRepository especialidades;
    private final RegimeTrabalhoRepository regimes;

    public CadastroService(PredioRepository predios, DepartamentoRepository departamentos, TurnoRepository turnos,
                           EspecialidadeRepository especialidades, RegimeTrabalhoRepository regimes) {
        this.predios = predios;
        this.departamentos = departamentos;
        this.turnos = turnos;
        this.especialidades = especialidades;
        this.regimes = regimes;
    }

    public List<PredioResponse> predios() {
        return predios.findAll(Sort.by("nome")).stream().map(PredioResponse::de).toList();
    }

    public List<DepartamentoResponse> departamentos(Long idPredio) {
        return departamentos.listar(idPredio).stream().map(DepartamentoResponse::de).toList();
    }

    public DepartamentoResponse departamento(Long id) {
        return departamentos.buscarCompleto(id)
                .map(DepartamentoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento", id));
    }

    public List<TurnoResponse> turnos() {
        return turnos.findAll(Sort.by("horaInicio")).stream().map(TurnoResponse::de).toList();
    }

    public List<EspecialidadeResponse> especialidades() {
        return especialidades.findAll(Sort.by("nome")).stream().map(EspecialidadeResponse::de).toList();
    }

    public List<RegimeResponse> regimes() {
        return regimes.findAll(Sort.by("cargaHorariaSemanal")).stream().map(RegimeResponse::de).toList();
    }

    // ------------------------------------------------------------ cadastros

    @Transactional
    public PredioResponse criarPredio(PredioRequest req) {
        if (predios.existsByNomeIgnoreCase(req.nome())) {
            throw new RegraNegocioException("UNICO", HttpStatus.CONFLICT, "Já existe um prédio chamado " + req.nome());
        }
        return PredioResponse.de(predios.save(new Predio(req.nome(), req.endereco())));
    }

    @Transactional
    public DepartamentoResponse criarDepartamento(DepartamentoRequest req) {
        // RN05: todo departamento pertence a um prédio que existe
        Predio predio = predios.findById(req.idPredio())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prédio", req.idPredio()));
        if (departamentos.existsByNomeIgnoreCaseAndPredio_Id(req.nome(), predio.getId())) {
            throw new RegraNegocioException("UNICO", HttpStatus.CONFLICT,
                    "Já existe o departamento " + req.nome() + " no prédio " + predio.getNome());
        }
        // Especialidades aceitas: cada id precisa existir (vira linhas em departamento_especialidade)
        Set<Especialidade> aceitas = new HashSet<>();
        for (Long id : req.idsEspecialidades()) {
            aceitas.add(especialidades.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade", id)));
        }
        Departamento novo = departamentos.save(new Departamento(req.nome(), predio, aceitas));
        return DepartamentoResponse.de(novo);
    }

    @Transactional
    public TurnoResponse criarTurno(TurnoRequest req) {
        if (turnos.existsByNomeIgnoreCase(req.nome())) {
            throw new RegraNegocioException("UNICO", HttpStatus.CONFLICT, "Já existe um turno chamado " + req.nome());
        }
        return TurnoResponse.de(turnos.save(new Turno(req.nome(), req.horaInicio(), req.horaFim(), req.duracaoHoras())));
    }
}

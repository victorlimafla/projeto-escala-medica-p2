package br.edu.eseg.escala;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Monta a escala do zero pela API, sobre uma cópia de data/escala.sqlite (só estrutura).
 * Especialidade 4 = Ortopedia; médico 1 = Ana Ribeiro (Ortopedia).
 */
@SpringBootTest
class CadastroApiTest {

    @DynamicPropertySource
    static void bancoTemporario(DynamicPropertyRegistry registry) throws IOException {
        Path copia = Files.createTempFile("escala-estrutura-", ".sqlite");
        Files.copy(Path.of("data/escala.sqlite"), copia, StandardCopyOption.REPLACE_EXISTING);
        copia.toFile().deleteOnExit();
        registry.add("spring.datasource.url", () -> "jdbc:sqlite:" + copia + "?foreign_keys=on&open_mode=2");
    }

    @Autowired
    private WebApplicationContext contexto;

    private MockMvc mvc;

    @BeforeEach
    void configurar() {
        mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    private org.springframework.test.web.servlet.ResultActions postJson(String url, String json) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void montaEscalaDoZeroPelaApi() throws Exception {
        postJson("/api/predios", "{\"nome\":\"Bloco A\",\"endereco\":\"Emergência\"}")
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
        postJson("/api/predios", "{\"nome\":\"Bloco A\"}").andExpect(status().isConflict());

        postJson("/api/departamentos", "{\"nome\":\"Ambulatório de Ortopedia\",\"idPredio\":1,\"idsEspecialidades\":[4]}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.predio").value("Bloco A"))
                .andExpect(jsonPath("$.especialidadesAceitas[0]").value("Ortopedia"));
        postJson("/api/departamentos", "{\"nome\":\"X\",\"idPredio\":99,\"idsEspecialidades\":[4]}").andExpect(status().isNotFound());
        postJson("/api/departamentos", "{\"nome\":\"X\",\"idPredio\":1,\"idsEspecialidades\":[999]}").andExpect(status().isNotFound());
        postJson("/api/departamentos", "{\"nome\":\"X\",\"idPredio\":1,\"idsEspecialidades\":[]}").andExpect(status().isBadRequest());

        postJson("/api/turnos", "{\"nome\":\"Manhã\",\"horaInicio\":\"07:00\",\"horaFim\":\"13:00\",\"duracaoHoras\":6}")
                .andExpect(status().isCreated());
        postJson("/api/turnos", "{\"nome\":\"Tarde\",\"horaInicio\":\"7h\",\"horaFim\":\"13:00\",\"duracaoHoras\":6}")
                .andExpect(status().isBadRequest());

        postJson("/api/alocacoes", "{\"idMedico\":1,\"idDepartamento\":1,\"idTurno\":1,\"diaSemana\":1}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departamento").value("Ambulatório de Ortopedia"));

        mvc.perform(get("/api/medicos/1/escala")).andExpect(jsonPath("$", hasSize(1)));
    }
}

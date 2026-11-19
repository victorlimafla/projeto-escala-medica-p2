package br.edu.eseg.escala.exception;

/** Gera HTTP 501: o endpoint existe, mas a consulta ainda não foi escrita. */
public class NaoImplementadoException extends RuntimeException {

    private final String relatorio;

    public NaoImplementadoException(String relatorio) {
        super(relatorio + " ainda não implementado: crie a consulta no PainelRepository e chame-a no PainelService");
        this.relatorio = relatorio;
    }

    public String getRelatorio() {
        return relatorio;
    }
}

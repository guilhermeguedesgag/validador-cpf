package br.com.exemplo.cadastro.service;

import java.time.LocalDate;

public record ResultadoConsulta(boolean encontrado, String nome, LocalDate nascimento, String situacao) {
    public static ResultadoConsulta naoEncontrado() {
        return new ResultadoConsulta(false, null, null, null);
    }
    public boolean regular() {
        return situacao != null && situacao.equalsIgnoreCase("Regular");
    }
}

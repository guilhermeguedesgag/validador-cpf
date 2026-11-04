package br.com.exemplo.cadastro.service;

import java.time.LocalDate;

public interface ConsultaCpfService {
    /** @param cpf somente dígitos */

    ResultadoConsulta consultar(String cpf, LocalDate nascimento);
//    ResultadoConsulta consultar(String cpf);
}

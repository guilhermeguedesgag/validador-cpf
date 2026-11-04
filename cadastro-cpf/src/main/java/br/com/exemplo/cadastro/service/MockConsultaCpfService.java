package br.com.exemplo.cadastro.service;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Base fictícia para desenvolvimento. */
@Service
@ConditionalOnProperty(name = "cpf.provider", havingValue = "mock", matchIfMissing = true)
public class MockConsultaCpfService implements ConsultaCpfService {

    private static final Map<String, ResultadoConsulta> BASE = Map.of(
        "52998224725", new ResultadoConsulta(true,
                    "Maria Silva", LocalDate.of(1990, 5, 15), "Regular"),

        "11144477735", new ResultadoConsulta(true,
                    "João Souza", LocalDate.of(1985, 12, 1), "Regular"),

        "36269364817", new ResultadoConsulta(true,
                    "Guilherme Antonio Guedes", LocalDate.of(1988, 8, 12), "Regular")
    );

    @Override
    public ResultadoConsulta consultar(String cpf, LocalDate nascimento) {
        return BASE.getOrDefault(cpf, ResultadoConsulta.naoEncontrado());
    }
}

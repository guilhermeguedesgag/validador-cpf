package br.com.exemplo.cadastro.service;

import br.com.exemplo.cadastro.model.CadastroForm;
import br.com.exemplo.cadastro.model.Pessoa;
import br.com.exemplo.cadastro.repository.PessoaRepository;
import java.text.Normalizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroService {

    private final PessoaRepository repo;
    private final ConsultaCpfService consulta;

    public CadastroService(PessoaRepository repo, ConsultaCpfService consulta) {
        this.repo = repo;
        this.consulta = consulta;
    }

    public Pessoa cadastrar(CadastroForm f, String sessionId) {
        String cpf = CpfValidator.somenteDigitos(f.getCpf());

        if (!CpfValidator.valido(cpf)) {
            throw new CadastroException("CPF inválido.");
        }
        if (repo.existsByCpfAndSessionId(cpf, sessionId)) {
            throw new CadastroException("Este CPF já está cadastrado.");
        }

        ResultadoConsulta r = consulta.consultar(cpf, f.getDataNascimento());

        if (!r.encontrado()) {
            throw new CadastroException("Dados incorretos. Verifique o CPF, nome e data de nascimento e tente novamente.");
        }

        boolean nomeCompativel = normaliza(r.nome()).equals(normaliza(f.getNome()));
        boolean dataNascCompativel = f.getDataNascimento().equals(r.nascimento());

        if (!nomeCompativel || !dataNascCompativel) {
            throw new CadastroException("Dados incorretos. O nome ou a data de nascimento não correspondem ao CPF informado. Verifique e tente novamente.");
        }

        if (!r.regular()) {
            throw new CadastroException("Cadastro não realizado. CPF com situação cadastral irregular: " + r.situacao() + ". Verifique e tente novamente.");
        }

        return repo.save(new Pessoa(f.getNome().trim(), cpf, f.getDataNascimento(), sessionId));
    }

    @Transactional
    public void limparSessao(String sessionId) {
        repo.deleteBySessionId(sessionId);
    }

    private static String normaliza(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .trim().replaceAll("\\s+", " ")
            .toUpperCase();
    }
}

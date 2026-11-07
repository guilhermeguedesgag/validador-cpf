package br.com.exemplo.cadastro.listener;

import br.com.exemplo.cadastro.service.CadastroService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

/**
 * Ouve eventos de sessão HTTP.
 * Quando uma sessão expira ou o usuário fecha o navegador,
 * apaga automaticamente todos os cadastros vinculados a ela.
 *
 * Registrado como bean do Spring (sem @WebListener) para permitir
 * injeção de dependência via construtor.
 */
@Component
public class SessaoListener implements HttpSessionListener {

    private final CadastroService service;

    public SessaoListener(CadastroService service) {
        this.service = service;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        service.limparSessao(event.getSession().getId());
    }
}

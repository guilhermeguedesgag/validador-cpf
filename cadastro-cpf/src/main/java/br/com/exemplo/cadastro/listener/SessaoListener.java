package br.com.exemplo.cadastro.listener;

import br.com.exemplo.cadastro.service.CadastroService;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

/**
 * Ouve eventos de sessão HTTP.
 * Quando uma sessão é destruída (expirou ou usuário fechou o navegador),
 * apaga automaticamente todos os cadastros vinculados a ela.
 */
@Component
@WebListener
public class SessaoListener implements HttpSessionListener {

    private final CadastroService service;

    public SessaoListener(CadastroService service) {
        this.service = service;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionId = event.getSession().getId();
        service.limparSessao(sessionId);
    }
}

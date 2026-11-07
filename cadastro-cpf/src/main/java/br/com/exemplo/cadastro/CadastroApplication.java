package br.com.exemplo.cadastro;

import br.com.exemplo.cadastro.listener.SessaoListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CadastroApplication {

    public static void main(String[] args) {
        SpringApplication.run(CadastroApplication.class, args);
    }

    /**
     * Registra o SessaoListener como servlet listener gerenciado pelo Spring,
     * permitindo injeção de dependência via construtor.
     */
    @Bean
    public ServletListenerRegistrationBean<SessaoListener> sessionListener(SessaoListener listener) {
        return new ServletListenerRegistrationBean<>(listener);
    }
}

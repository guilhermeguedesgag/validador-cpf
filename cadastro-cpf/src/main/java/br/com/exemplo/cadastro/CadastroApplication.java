package br.com.exemplo.cadastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CadastroApplication {
    public static void main(String[] args) {
        SpringApplication.run(CadastroApplication.class, args);
    }
}

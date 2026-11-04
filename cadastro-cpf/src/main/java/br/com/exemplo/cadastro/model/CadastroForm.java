package br.com.exemplo.cadastro.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class CadastroForm {

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe o CPF")
    private String cpf;

    @NotNull(message = "Informe a data de nascimento")
    @Past(message = "A data de nascimento deve estar no passado")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNascimento;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}

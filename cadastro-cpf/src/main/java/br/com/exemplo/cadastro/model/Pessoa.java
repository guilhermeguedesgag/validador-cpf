package br.com.exemplo.cadastro.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Pessoa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf; // somente dígitos

    @Column(nullable = false)
    private LocalDate dataNascimento;

    protected Pessoa() {}

    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
}

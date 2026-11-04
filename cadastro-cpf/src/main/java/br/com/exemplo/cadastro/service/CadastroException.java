package br.com.exemplo.cadastro.service;

/** Erro de regra de negócio; a mensagem é exibida ao usuário. */
public class CadastroException extends RuntimeException {
    public CadastroException(String message) { super(message); }
}

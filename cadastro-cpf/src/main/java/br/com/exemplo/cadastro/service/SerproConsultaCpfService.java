package br.com.exemplo.cadastro.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Integração com a API Consulta CPF do Serpro (base da Receita Federal).
 * Confira URL, formato de resposta e códigos HTTP na documentação atual do Serpro
 * e no contrato que você assinar; ajuste aqui se algo for diferente.
 */
@Service
@ConditionalOnProperty(name = "cpf.provider", havingValue = "serpro")
public class SerproConsultaCpfService implements ConsultaCpfService {

    private static final DateTimeFormatter NASC = DateTimeFormatter.ofPattern("ddMMyyyy");

    private final RestClient http = RestClient.create();
    private final String baseUrl, tokenUrl, key, secret;
    private String token;
    private Instant expira = Instant.EPOCH;

    public SerproConsultaCpfService(
            @Value("${serpro.base-url}") String baseUrl,
            @Value("${serpro.token-url}") String tokenUrl,
            @Value("${serpro.consumer-key}") String key,
            @Value("${serpro.consumer-secret}") String secret) {
        this.baseUrl = baseUrl; this.tokenUrl = tokenUrl; this.key = key; this.secret = secret;
    }

    @Override
    public ResultadoConsulta consultar(String cpf, LocalDate nascimento) {
        try {
            Map<String, Object> r = http.get()
                .uri(baseUrl + "/cpf/{cpf}", cpf)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
            if (r == null) return ResultadoConsulta.naoEncontrado();

            String nome = (String) r.get("nome");
            String nasc = (String) r.get("nascimento");
            @SuppressWarnings("unchecked")
            Map<String, Object> sit = (Map<String, Object>) r.get("situacao");
            String situacao = sit == null ? null : (String) sit.get("descricao");

            return new ResultadoConsulta(true, nome,
                nasc == null ? null : LocalDate.parse(nasc, NASC), situacao);
        } catch (HttpClientErrorException.NotFound e) {
            return ResultadoConsulta.naoEncontrado();
        } catch (RestClientException e) {
            throw new CadastroException("Serviço de consulta de CPF indisponível. Tente novamente em instantes.");
        }
    }

    private synchronized String token() {
        if (token != null && Instant.now().isBefore(expira)) return token;
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        Map<String, Object> r = http.post()
            .uri(tokenUrl)
            .headers(h -> h.setBasicAuth(key, secret))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
        token = (String) r.get("access_token");
        int seg = ((Number) r.getOrDefault("expires_in", 3600)).intValue();
        expira = Instant.now().plusSeconds(Math.max(seg - 60, 30));
        return token;
    }
}

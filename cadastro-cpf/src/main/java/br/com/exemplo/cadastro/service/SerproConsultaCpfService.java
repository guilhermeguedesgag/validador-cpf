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
 *
 * Suporta dois modos de autenticação:
 * - Token fixo: defina serpro.static-token com o Bearer token (ex: ambiente trial)
 * - OAuth2 client_credentials: defina serpro.consumer-key e serpro.consumer-secret
 */
@Service
@ConditionalOnProperty(name = "cpf.provider", havingValue = "serpro")
public class SerproConsultaCpfService implements ConsultaCpfService {

    private static final DateTimeFormatter NASC_FMT = DateTimeFormatter.ofPattern("ddMMyyyy");

    private final RestClient http = RestClient.create();
    private final String baseUrl, tokenUrl, key, secret, staticToken;
    private String cachedToken;
    private Instant expira = Instant.EPOCH;

    public SerproConsultaCpfService(
            @Value("${serpro.base-url}") String baseUrl,
            @Value("${serpro.token-url}") String tokenUrl,
            @Value("${serpro.consumer-key:}") String key,
            @Value("${serpro.consumer-secret:}") String secret,
            @Value("${serpro.static-token:}") String staticToken) {
        this.baseUrl = baseUrl;
        this.tokenUrl = tokenUrl;
        this.key = key;
        this.secret = secret;
        this.staticToken = staticToken;
    }

    @Override
    public ResultadoConsulta consultar(String cpf, LocalDate nascimento) {
        // A API Trial exige a data de nascimento na URL: /cpf/{cpf}/{ddMMyyyy}
        String nascStr = nascimento.format(NASC_FMT);
        try {
            Map<String, Object> r = http.get()
                .uri(baseUrl + "/cpf/{cpf}/{nasc}", cpf, nascStr)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token())
                .header(HttpHeaders.ACCEPT, "application/json")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

            if (r == null) return ResultadoConsulta.naoEncontrado();

            String nome = (String) r.get("nome");
            String nascRetorno = (String) r.get("nascimento");
            @SuppressWarnings("unchecked")
            Map<String, Object> sit = (Map<String, Object>) r.get("situacao");
            String situacao = sit == null ? null : (String) sit.get("descricao");

            return new ResultadoConsulta(true, nome,
                nascRetorno == null ? null : LocalDate.parse(nascRetorno, NASC_FMT), situacao);

        } catch (HttpClientErrorException.NotFound e) {
            return ResultadoConsulta.naoEncontrado();
        } catch (RestClientException e) {
            throw new CadastroException("Serviço de consulta de CPF indisponível. Tente novamente em instantes.");
        }
    }

    private synchronized String token() {
        // Se há token fixo configurado (ex: trial), usa diretamente sem OAuth2
        if (!staticToken.isBlank()) return staticToken;

        // Caso contrário, obtém/renova via OAuth2 client_credentials
        if (cachedToken != null && Instant.now().isBefore(expira)) return cachedToken;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        Map<String, Object> r = http.post()
            .uri(tokenUrl)
            .headers(h -> h.setBasicAuth(key, secret))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});

        cachedToken = (String) r.get("access_token");
        int seg = ((Number) r.getOrDefault("expires_in", 3600)).intValue();
        expira = Instant.now().plusSeconds(Math.max(seg - 60, 30));
        return cachedToken;
    }
}

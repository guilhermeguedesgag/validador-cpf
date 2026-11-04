# Cadastro com validação de CPF (Spring Boot + Thymeleaf)

## Rodar
    mvn spring-boot:run
Acesse http://localhost:8080

## Testar com o provedor mock (padrão)
- Maria Silva, 529.982.247-25, 15/05/1990 -> cadastra
- João Souza, 111.444.777-35, 01/12/1985 -> cadastra
- Mesmo CPF com nome ou data diferentes -> "CPF não pertence à pessoa informada."
- CPF com dígitos inválidos -> "CPF inválido."

## Usar a base real (Serpro / Receita)
1. Contrate a API Consulta CPF do Serpro e obtenha consumer key/secret.
2. Defina SERPRO_KEY e SERPRO_SECRET no ambiente.
3. Em application.properties: cpf.provider=serpro
4. Confira URL e formato de resposta na documentação do Serpro (ajuste SerproConsultaCpfService se preciso).

## Estrutura
- controller/CadastroController  -> telas e POST
- service/CadastroService        -> regra: valida dígitos, consulta, compara nome + nascimento
- service/ConsultaCpfService     -> interface (Mock e Serpro)
- templates/*.html               -> front Thymeleaf


## Como rodar esse projeto(fazendo)

Execute o arquivo CadastroApplication.java
Depois execute no seu navegador http://localhost:8080
Depois tente colocar os dados mocados que estão neste documento, 
Se colocar diferente, irá notar que irá exibir um erro
Acesse a API demo da Serpro para realizar as configurações:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/en/demonstracao/demo-consulta-cpf-df-v3/

Documentação da Serpro:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/quick_start/#como-autenticar-na-api-consulta-cpf

Dados mocados:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/quick_start/#como-autenticar-na-api-consulta-cpf

API de deste:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/demonstracao/demo-consulta-cpf-df-v3/
# Cadastro com validação de CPF no Spring Boot

## Objetivo
O objetivo da validação é impedir que tenha cadastros de pessoa física duplicados no sistema, como a API valida na Serpro que valida na Receita Federal, se porventura nome, data de nasc e cpf estarem divergentes, a API não irá cadastrar, emitando um aviso.

<br><br><br>


## 1º Defina a variável de ambiente
Vá até o arquivo `application.properties` , nele você irá encontrar duas variáveis de ambiente, um de mock para realizar testes com dados locais em seu arquivo 'MockConsultaCPFService.java' e o outro aponta para a API Trial da Serpro.

<br><br>

## 2º Iniciando o projeto
Para iniciar o projeto, deve-se excutar o comando abaixo no terminal na pasta principal do projeto:
    mvn spring-boot:run
    
A seguir, tente acessar o http://localhost:8080



Deverá aparecer uma tela do front para inserir o nome, cpf e data de nasc.

## 3° Testando localmente (ambiente mockado)
- Maria Silva, 529.982.247-25, 15/05/1990
- João Souza, 111.444.777-35, 01/12/1985
- Guilherme Guedes, 111.222.333-44, 12/08/1988

Todos esses dados estão dentro do arquivo 'MockConsultaCPFService.java' onde você pode inserir mais alguns se quiser.

## 4º Usar a base de homologação da Serpro(Trial)

Para testar a API de Trial da Serpro, você deverá ter definido a varíavel de ambiente (Passo 1) `serpro`.
 

Abaixo dados fictícios no ambiente Trial da Serpro

 | Nome             | CPF            | Nascimento | Situação                  |
| ---------------- | -------------- | ---------- | ------------------------- |
| LETICIA CARVALHO | 405.321.768-71 | 22/03/1982 | SUSPENSA                  |
| LETICIA PEREIRA  | 076.918.523-12 | 13/08/2000 | PENDENTE DE REGULARIZACAO |
| RONE PEREIRA     | 404.428.201-35 | 14/11/1970 | REGULAR                   |
| LUCAS GUIMARAES  | 088.499.798-78 | 23/03/1980 | TITULAR FALECIDO          |
| LETICIA CARVALHO | 302.720.950-79 | 03/02/2009 | MENOR DE 18 ANOS
|                  | 983.025.147-05 | 05/10/2009 | NULA
        

### Testes para serem feitos
Com usuários regulares, testem cadastrar com dados divergentes, exemplo trocando letras do nome ou data de nascimento, e verifique se o sistema aceita essas informações.



<br><br><br>
# Informações Gerais
### Documentação da Serpro:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/quick_start/#como-autenticar-na-api-consulta-cpf

### Todos os dados mocados para testar a Trial:
https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/quick_start/#como-autenticar-na-api-consulta-cpf

### API de deste
**https://apicenter.estaleiro.serpro.gov.br/documentacao/consulta-cpf/pt/demonstracao/demo-consulta-cpf-df-v3/**


### O endpoint de `GET` é:
 **https://gateway.apiserpro.serpro.gov.br/consulta-cpf-df-trial/v3/cpf/{cpf}/{dataNascimento}**
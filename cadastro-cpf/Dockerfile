# ---- Estágio 1: build ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copia o pom.xml primeiro para aproveitar o cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -q
# Copia o código fonte e compila
COPY src ./src
RUN mvn package -DskipTests -q

# ---- Estágio 2: runtime ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/cadastro-cpf-0.0.1-SNAPSHOT.jar app.jar
# Porta padrão do Spring Boot
EXPOSE 8080
# Variável de ambiente para o Render informar a porta dinamicamente
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]

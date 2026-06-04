FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho dentro do contentor
WORKDIR /app

# Copia o JAR compilado para dentro do contentor
COPY target/BattleshipGamePlayer-2.0.jar app.jar

# Comando para executar o jogo
ENTRYPOINT ["java", "-jar", "app.jar"]


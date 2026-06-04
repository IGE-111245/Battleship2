FROM ubuntu:24.04

# Evitar perguntas interativas durante a instalação
ENV DEBIAN_FRONTEND=noninteractive

# Instalar o JDK 21, Maven, Git e utilitários básicos
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    maven \
    git \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Definir o diretório padrão dentro do container
WORKDIR /workspace
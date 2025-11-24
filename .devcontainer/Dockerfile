FROM mcr.microsoft.com/devcontainers/base:ubuntu-24.04

# Install Java 17, Maven, and MySQL client
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    openjdk-17-jdk \
    maven \
    mysql-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

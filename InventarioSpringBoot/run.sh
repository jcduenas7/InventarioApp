#!/bin/bash
echo "🚀 Iniciando Sistema de Inventario..."
echo "📦 Compilando proyecto..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo "🔥 Iniciando aplicación..."
    java -jar target/InventarioSpringBoot.jar --spring.profiles.active=h2
else
    echo "❌ Error en la compilación"
    exit 1
fi

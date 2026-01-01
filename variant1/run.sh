#!/bin/bash

# 🧪 Script de compilation et démarrage - Variante A (Jersey + Hibernate)
# Auteur: Halmaoui Abdellah
# Date: Novembre 2025

echo "🅰️  VARIANTE A - Jersey (JAX-RS) + Hibernate"
echo "=============================================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Vérification de Java
echo "🔍 Vérification de Java..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java n'est pas installé${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "${GREEN}✅ Java $JAVA_VERSION détecté${NC}"

if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}❌ Java 17+ requis (version actuelle: $JAVA_VERSION)${NC}"
    exit 1
fi

# Vérification de Maven
echo "🔍 Vérification de Maven..."
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven n'est pas installé${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Maven détecté${NC}"

# Vérification de PostgreSQL (Docker)
echo "🔍 Vérification de PostgreSQL..."
if ! PGPASSWORD=postgres pg_isready -h localhost -U postgres -p 5432 &> /dev/null; then
    echo -e "${RED}❌ PostgreSQL n'est pas accessible sur localhost:5432${NC}"
    echo "Veuillez démarrer Docker avec: docker-compose up -d"
    exit 1
fi
echo -e "${GREEN}✅ PostgreSQL est accessible${NC}"

# Vérification de la base de données
echo "🔍 Vérification de la base de données benchmark_db..."
DB_EXISTS=$(PGPASSWORD=postgres psql -h localhost -U postgres -l | grep -w benchmark_db | wc -l)
if [ "$DB_EXISTS" -eq 0 ]; then
    echo -e "${YELLOW}⚠️  Base de données 'benchmark_db' n'existe pas${NC}"
    echo "Création de la base de données..."
    PGPASSWORD=postgres createdb -h localhost -U postgres benchmark_db
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Base de données créée${NC}"
    else
        echo -e "${RED}❌ Erreur lors de la création de la base${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✅ Base de données 'benchmark_db' existe${NC}"
fi

echo ""
echo "🏗️  Compilation du projet..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erreur de compilation${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Compilation réussie${NC}"
echo ""

# Lancement de l'application
echo "🚀 Démarrage de l'application..."
echo ""

java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar

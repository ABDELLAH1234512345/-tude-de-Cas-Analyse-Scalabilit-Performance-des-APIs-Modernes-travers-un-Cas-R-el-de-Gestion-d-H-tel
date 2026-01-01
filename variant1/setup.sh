#!/bin/bash

# 🎯 Script de démarrage complet - Variante A
# Ce script gère tout : vérification, compilation, démarrage et tests

echo "🅰️  SETUP COMPLET - VARIANTE A"
echo "=============================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Fonction pour afficher un message avec style
info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

success() {
    echo -e "${GREEN}✅ $1${NC}"
}

error() {
    echo -e "${RED}❌ $1${NC}"
}

warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Menu
echo "Que voulez-vous faire ?"
echo ""
echo "1) Compilation seulement"
echo "2) Démarrage seulement (nécessite compilation préalable)"
echo "3) Compilation + Démarrage"
echo "4) Compilation + Démarrage + Tests fonctionnels"
echo "5) Compilation + Démarrage + Tests JMeter"
echo "6) Tests fonctionnels seulement (serveur doit être lancé)"
echo "7) Tests JMeter seulement (serveur doit être lancé)"
echo ""
read -p "Votre choix [1-7]: " choice

case $choice in
    1)
        info "Compilation du projet..."
        cd variant-a
        mvn clean package -DskipTests
        if [ $? -eq 0 ]; then
            success "Compilation réussie"
        else
            error "Échec de la compilation"
            exit 1
        fi
        ;;
    
    2)
        info "Démarrage du serveur..."
        cd variant-a
        if [ ! -f "target/benchmark-variant-a-1.0-SNAPSHOT.jar" ]; then
            error "Fichier JAR non trouvé. Compilez d'abord le projet (option 1 ou 3)"
            exit 1
        fi
        java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar
        ;;
    
    3)
        info "Compilation du projet..."
        cd variant-a
        mvn clean package -DskipTests
        if [ $? -eq 0 ]; then
            success "Compilation réussie"
            echo ""
            info "Démarrage du serveur..."
            java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar
        else
            error "Échec de la compilation"
            exit 1
        fi
        ;;
    
    4)
        info "Compilation du projet..."
        cd variant-a
        mvn clean package -DskipTests
        if [ $? -ne 0 ]; then
            error "Échec de la compilation"
            exit 1
        fi
        success "Compilation réussie"
        
        echo ""
        info "Démarrage du serveur en arrière-plan..."
        java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar > server.log 2>&1 &
        SERVER_PID=$!
        
        echo "PID du serveur: $SERVER_PID"
        info "Attente du démarrage du serveur (15 secondes)..."
        sleep 15
        
        if curl -s http://localhost:8080/items > /dev/null 2>&1; then
            success "Serveur démarré"
            echo ""
            info "Lancement des tests fonctionnels..."
            ./test_api.sh
            
            echo ""
            warning "Arrêt du serveur (PID: $SERVER_PID)..."
            kill $SERVER_PID
            success "Serveur arrêté"
        else
            error "Le serveur n'a pas démarré correctement"
            kill $SERVER_PID 2>/dev/null
            exit 1
        fi
        ;;
    
    5)
        info "Compilation du projet..."
        cd variant-a
        mvn clean package -DskipTests
        if [ $? -ne 0 ]; then
            error "Échec de la compilation"
            exit 1
        fi
        success "Compilation réussie"
        
        echo ""
        info "Démarrage du serveur en arrière-plan..."
        java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar > server.log 2>&1 &
        SERVER_PID=$!
        
        echo "PID du serveur: $SERVER_PID"
        info "Attente du démarrage complet (30 secondes pour génération des données)..."
        sleep 30
        
        if curl -s http://localhost:8080/items > /dev/null 2>&1; then
            success "Serveur démarré"
            echo ""
            info "Lancement des tests JMeter..."
            ./jmeter_tests.sh
            
            echo ""
            warning "Arrêt du serveur (PID: $SERVER_PID)..."
            kill $SERVER_PID
            success "Serveur arrêté"
        else
            error "Le serveur n'a pas démarré correctement"
            kill $SERVER_PID 2>/dev/null
            exit 1
        fi
        ;;
    
    6)
        info "Lancement des tests fonctionnels..."
        cd variant-a
        if ! curl -s http://localhost:8080/items > /dev/null 2>&1; then
            error "Serveur non accessible. Démarrez-le d'abord"
            exit 1
        fi
        ./test_api.sh
        ;;
    
    7)
        info "Lancement des tests JMeter..."
        cd variant-a
        if ! curl -s http://localhost:8080/items > /dev/null 2>&1; then
            error "Serveur non accessible. Démarrez-le d'abord"
            exit 1
        fi
        ./jmeter_tests.sh
        ;;
    
    *)
        error "Choix invalide"
        exit 1
        ;;
esac

echo ""
success "Terminé!"

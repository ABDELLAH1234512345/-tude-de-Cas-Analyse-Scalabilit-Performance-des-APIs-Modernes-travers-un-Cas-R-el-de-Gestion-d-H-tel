#!/bin/bash

# 🚀 Script de démarrage complet avec monitoring
# Démarre Docker Compose + les 3 variantes

echo "🚀 DÉMARRAGE COMPLET DU BENCHMARK AVEC MONITORING"
echo "=================================================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Fonction d'affichage
info() { echo -e "${BLUE}ℹ️  $1${NC}"; }
success() { echo -e "${GREEN}✅ $1${NC}"; }
error() { echo -e "${RED}❌ $1${NC}"; }
warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }

# Vérifier Docker
info "Vérification de Docker..."
if ! command -v docker &> /dev/null; then
    error "Docker n'est pas installé"
    exit 1
fi

if ! docker info &> /dev/null; then
    error "Docker daemon n'est pas démarré"
    exit 1
fi
success "Docker OK"

# Vérifier docker-compose
info "Vérification de docker-compose..."
if ! command -v docker-compose &> /dev/null; then
    error "docker-compose n'est pas installé"
    exit 1
fi
success "docker-compose OK"

echo ""
info "Démarrage du stack de monitoring (Docker Compose)..."
docker-compose up -d

if [ $? -ne 0 ]; then
    error "Échec du démarrage de Docker Compose"
    exit 1
fi

echo ""
info "Attente du démarrage des services (30 secondes)..."
sleep 30

echo ""
info "Vérification des services Docker..."
docker-compose ps

echo ""
success "Stack de monitoring démarré !"
echo ""
echo "📊 Services disponibles:"
echo "   - Prometheus:          http://localhost:9094"
echo "   - Grafana:             http://localhost:3003  (admin/admin)"
echo "   - Node Exporter:       http://localhost:9100/metrics"
echo "   - PostgreSQL Exporter: http://localhost:9187/metrics"
echo "   - PostgreSQL:          localhost:5432"
echo ""

# Demander quelle variante démarrer
echo "Quelle variante voulez-vous démarrer ?"
echo ""
echo "1) Variante A (Jersey + Hibernate)        - Port 8080"
echo "2) Variante C (Spring Boot @RestController) - Port 8081"
echo "3) Variante D (Spring Data REST)           - Port 8082"
echo "4) Toutes les variantes (en arrière-plan)"
echo "5) Aucune (monitoring seulement)"
echo ""
read -p "Votre choix [1-5]: " choice

case $choice in
    1)
        info "Démarrage Variante A..."
        cd variant-a
        ./run.sh
        ;;
    
    2)
        info "Démarrage Variante C..."
        warning "TODO: Configurer le port 8081 dans application.properties"
        mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
        ;;
    
    3)
        info "Démarrage Variante D..."
        warning "TODO: Configurer le port 8082 dans application.properties"
        mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
        ;;
    
    4)
        info "Démarrage des 3 variantes en arrière-plan..."
        
        # Variante A
        cd variant-a
        nohup java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar > variant-a.log 2>&1 &
        echo $! > variant-a.pid
        success "Variante A démarrée (PID: $(cat variant-a.pid))"
        cd ..
        
        # Variante C
        warning "Variante C: à démarrer manuellement sur port 8081"
        
        # Variante D
        warning "Variante D: à démarrer manuellement sur port 8082"
        
        echo ""
        info "Logs disponibles:"
        echo "   - Variante A: variant-a/variant-a.log"
        echo ""
        info "Pour arrêter:"
        echo "   kill \$(cat variant-a/variant-a.pid)"
        ;;
    
    5)
        success "Monitoring seulement - pas de variante démarrée"
        ;;
    
    *)
        error "Choix invalide"
        exit 1
        ;;
esac

echo ""
success "Démarrage terminé !"
echo ""
echo "📊 Prochaines étapes:"
echo "   1. Vérifier Prometheus targets: http://localhost:9094/targets"
echo "   2. Ouvrir Grafana: http://localhost:3003"
echo "   3. Aller dans Dashboards → Benchmark → Comparaison"
echo "   4. Lancer les tests JMeter"
echo ""
echo "Pour arrêter le monitoring:"
echo "   docker-compose down"

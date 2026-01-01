#!/bin/bash

# 🔥 Script de tests JMeter pour Variante C
# Auteur: Halmaoui Abdellah
# Date: Novembre 2025

echo "🔥 TESTS JMETER - VARIANTE C (Spring MVC + Hibernate)"
echo "================================================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
BASE_URL="http://localhost:8081"
JMETER_DIR="../jmeter"
RESULTS_DIR="./jmeter-results"

# Vérifier JMeter
if ! command -v jmeter &> /dev/null; then
    echo -e "${RED}❌ JMeter n'est pas installé ou pas dans le PATH${NC}"
    echo "Installation: brew install jmeter (macOS) ou télécharger depuis https://jmeter.apache.org/"
    exit 1
fi

echo -e "${GREEN}✅ JMeter détecté${NC}"

# Vérifier que le serveur est accessible
echo "🔍 Vérification du serveur..."
if ! curl -s "$BASE_URL/items" > /dev/null 2>&1; then
    echo -e "${RED}❌ Serveur non accessible sur $BASE_URL${NC}"
    echo "Veuillez démarrer le serveur avec: cd variant-c && mvn spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✅ Serveur accessible${NC}"
echo ""

# Créer le répertoire de résultats
mkdir -p "$RESULTS_DIR"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo "📊 Lancement des scénarios de test JMeter..."
echo ""

# Scénario 1: Lecture intensive (Read-Heavy)
echo -e "${BLUE}[1/4]${NC} 📖 Scénario 1: Lecture intensive (Read-Heavy)"
jmeter -n -t "$JMETER_DIR/read_heavy_scenario.jmx" \
    -l "$RESULTS_DIR/read_heavy_${TIMESTAMP}.jtl" \
    -e -o "$RESULTS_DIR/read_heavy_${TIMESTAMP}_html" \
    -Jhost=localhost -Jport=8081

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Scénario 1 terminé${NC}"
else
    echo -e "${RED}❌ Scénario 1 échoué${NC}"
fi
echo ""

# Scénario 2: Filtrage avec JOIN
echo -e "${BLUE}[2/4]${NC} 🔗 Scénario 2: Filtrage avec JOIN"
jmeter -n -t "$JMETER_DIR/join_filter_scenario.jmx" \
    -l "$RESULTS_DIR/join_filter_${TIMESTAMP}.jtl" \
    -e -o "$RESULTS_DIR/join_filter_${TIMESTAMP}_html" \
    -Jhost=localhost -Jport=8081

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Scénario 2 terminé${NC}"
else
    echo -e "${RED}❌ Scénario 2 échoué${NC}"
fi
echo ""

# Scénario 3: Opérations mixtes (Mixed)
echo -e "${BLUE}[3/4]${NC} 🔄 Scénario 3: Opérations mixtes (Mixed)"
jmeter -n -t "$JMETER_DIR/mixed_scenario_fixed.jmx" \
    -l "$RESULTS_DIR/mixed_${TIMESTAMP}.jtl" \
    -e -o "$RESULTS_DIR/mixed_${TIMESTAMP}_html" \
    -Jhost=localhost -Jport=8081

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Scénario 3 terminé${NC}"
else
    echo -e "${RED}❌ Scénario 3 échoué${NC}"
fi
echo ""

# Scénario 4: Payloads lourds (Heavy Body)
echo -e "${BLUE}[4/4]${NC} 💾 Scénario 4: Payloads lourds (Heavy Body)"
jmeter -n -t "$JMETER_DIR/heavy_body_scenario.jmx" \
    -l "$RESULTS_DIR/heavy_body_${TIMESTAMP}.jtl" \
    -e -o "$RESULTS_DIR/heavy_body_${TIMESTAMP}_html" \
    -Jhost=localhost -Jport=8081

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Scénario 4 terminé${NC}"
else
    echo -e "${RED}❌ Scénario 4 échoué${NC}"
fi
echo ""

echo "=" $(printf '=%.0s' {1..60})
echo ""
echo "✅ Tous les tests JMeter sont terminés!"
echo ""
echo "📂 Résultats disponibles dans: $RESULTS_DIR/"
echo ""
echo "📊 Rapports HTML générés:"
echo "   - $RESULTS_DIR/read_heavy_${TIMESTAMP}_html/index.html"
echo "   - $RESULTS_DIR/join_filter_${TIMESTAMP}_html/index.html"
echo "   - $RESULTS_DIR/mixed_${TIMESTAMP}_html/index.html"
echo "   - $RESULTS_DIR/heavy_body_${TIMESTAMP}_html/index.html"
echo ""
echo "💡 Ouvrez les fichiers index.html dans un navigateur pour voir les résultats détaillés"
echo ""

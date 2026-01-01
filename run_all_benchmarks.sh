#!/bin/bash

# 🚀 Master Benchmark Runner - Tests tous les variants
# Auteur: Halmaoui Abdellah
# Date: Novembre 2025

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║  🚀 BENCHMARK MASTER RUNNER - Web Services REST Performance   ║"
echo "║  Tests JMeter sur les 3 variantes d'implémentation           ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
NC='\033[0m'

# Configuration
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULTS_DIR="benchmark-results-${TIMESTAMP}"

echo -e "${CYAN}📁 Création du répertoire de résultats: $RESULTS_DIR${NC}"
mkdir -p "$RESULTS_DIR"
echo ""

# ============================================================================
# ÉTAPE 1: Vérification des pré-requis
# ============================================================================
echo -e "${BLUE}[ÉTAPE 1/4]${NC} 🔍 Vérification des pré-requis..."
echo ""

# Vérifier JMeter
if ! command -v jmeter &> /dev/null; then
    echo -e "${RED}❌ JMeter n'est pas installé ou pas dans le PATH${NC}"
    echo "Installation: brew install jmeter (macOS) ou télécharger depuis https://jmeter.apache.org/"
    exit 1
fi
echo -e "${GREEN}✅ JMeter détecté ($(jmeter -v 2>&1 | head -n 1))${NC}"

# Vérifier que les 3 serveurs sont accessibles
echo ""
echo "🔍 Vérification de l'accessibilité des serveurs..."
echo ""

VARIANT_A_RUNNING=false
VARIANT_C_RUNNING=false
VARIANT_D_RUNNING=false

# Variant A (Jersey + Hibernate) - Port 8080
if curl -s "http://localhost:8080/items" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Variant A (Jersey + Hibernate) accessible sur http://localhost:8080${NC}"
    VARIANT_A_RUNNING=true
else
    echo -e "${YELLOW}⚠️  Variant A non accessible sur http://localhost:8080${NC}"
fi

# Variant C (Spring MVC + Hibernate) - Port 8081
if curl -s "http://localhost:8081/items" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Variant C (Spring MVC + Hibernate) accessible sur http://localhost:8081${NC}"
    VARIANT_C_RUNNING=true
else
    echo -e "${YELLOW}⚠️  Variant C non accessible sur http://localhost:8081${NC}"
fi

# Variant D (Spring Data REST) - Port 8082
if curl -s "http://localhost:8082/items" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Variant D (Spring Data REST) accessible sur http://localhost:8082${NC}"
    VARIANT_D_RUNNING=true
else
    echo -e "${YELLOW}⚠️  Variant D non accessible sur http://localhost:8082${NC}"
fi

echo ""

# Déterminer quels tests exécuter
if ! $VARIANT_A_RUNNING && ! $VARIANT_C_RUNNING && ! $VARIANT_D_RUNNING; then
    echo -e "${RED}❌ ERREUR: Aucun variant n'est accessible!${NC}"
    echo ""
    echo "Veuillez démarrer au moins un variant:"
    echo "  - Variant A: cd variant-a && ./run.sh"
    echo "  - Variant C: cd variant-c && mvn spring-boot:run"
    echo "  - Variant D: cd variant-d && mvn spring-boot:run"
    exit 1
fi

echo -e "${GREEN}✅ Pré-requis validés${NC}"
echo ""

# ============================================================================
# ÉTAPE 2: Tests Variant A (Jersey + Hibernate)
# ============================================================================
if $VARIANT_A_RUNNING; then
    echo -e "${BLUE}[ÉTAPE 2/4]${NC} 🔥 Tests JMeter - Variant A (Jersey + Hibernate)"
    echo "══════════════════════════════════════════════════════════════"
    echo ""

    cd variant-a || exit 1

    if [ -f "jmeter_tests.sh" ]; then
        ./jmeter_tests.sh

        # Copier les résultats dans le dossier global
        if [ -d "jmeter-results" ]; then
            mkdir -p "../$RESULTS_DIR/variant-a"
            cp -r jmeter-results/* "../$RESULTS_DIR/variant-a/"
            echo -e "${GREEN}✅ Résultats Variant A copiés dans $RESULTS_DIR/variant-a/${NC}"
        fi
    else
        echo -e "${RED}❌ Script jmeter_tests.sh introuvable pour Variant A${NC}"
    fi

    cd ..
    echo ""
else
    echo -e "${YELLOW}[ÉTAPE 2/4] ⏭️  Tests Variant A ignorés (serveur non accessible)${NC}"
    echo ""
fi

# ============================================================================
# ÉTAPE 3: Tests Variant C (Spring MVC + Hibernate)
# ============================================================================
if $VARIANT_C_RUNNING; then
    echo -e "${BLUE}[ÉTAPE 3/4]${NC} 🔥 Tests JMeter - Variant C (Spring MVC + Hibernate)"
    echo "══════════════════════════════════════════════════════════════"
    echo ""

    cd variant-c || exit 1

    if [ -f "jmeter_tests.sh" ]; then
        ./jmeter_tests.sh

        # Copier les résultats dans le dossier global
        if [ -d "jmeter-results" ]; then
            mkdir -p "../$RESULTS_DIR/variant-c"
            cp -r jmeter-results/* "../$RESULTS_DIR/variant-c/"
            echo -e "${GREEN}✅ Résultats Variant C copiés dans $RESULTS_DIR/variant-c/${NC}"
        fi
    else
        echo -e "${RED}❌ Script jmeter_tests.sh introuvable pour Variant C${NC}"
    fi

    cd ..
    echo ""
else
    echo -e "${YELLOW}[ÉTAPE 3/4] ⏭️  Tests Variant C ignorés (serveur non accessible)${NC}"
    echo ""
fi

# ============================================================================
# ÉTAPE 4: Tests Variant D (Spring Data REST)
# ============================================================================
if $VARIANT_D_RUNNING; then
    echo -e "${BLUE}[ÉTAPE 4/4]${NC} 🔥 Tests JMeter - Variant D (Spring Data REST)"
    echo "══════════════════════════════════════════════════════════════"
    echo ""

    cd variant-d || exit 1

    if [ -f "jmeter_tests.sh" ]; then
        ./jmeter_tests.sh

        # Copier les résultats dans le dossier global
        if [ -d "jmeter-results" ]; then
            mkdir -p "../$RESULTS_DIR/variant-d"
            cp -r jmeter-results/* "../$RESULTS_DIR/variant-d/"
            echo -e "${GREEN}✅ Résultats Variant D copiés dans $RESULTS_DIR/variant-d/${NC}"
        fi
    else
        echo -e "${RED}❌ Script jmeter_tests.sh introuvable pour Variant D${NC}"
    fi

    cd ..
    echo ""
else
    echo -e "${YELLOW}[ÉTAPE 4/4] ⏭️  Tests Variant D ignorés (serveur non accessible)${NC}"
    echo ""
fi

# ============================================================================
# RÉSUMÉ FINAL
# ============================================================================
echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║                    ✅ BENCHMARK TERMINÉ                        ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# Créer un fichier de résumé
SUMMARY_FILE="$RESULTS_DIR/SUMMARY.txt"
{
    echo "═══════════════════════════════════════════════════════════════"
    echo "  BENCHMARK REST WEB SERVICES - Résumé des tests"
    echo "  Date: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "═══════════════════════════════════════════════════════════════"
    echo ""
    echo "VARIANTS TESTÉS:"
    echo ""

    if $VARIANT_A_RUNNING; then
        echo "  ✅ Variant A (Jersey + Hibernate) - Port 8080"
        echo "     └─ Résultats: $RESULTS_DIR/variant-a/"
    else
        echo "  ⏭️  Variant A (Jersey + Hibernate) - Non testé"
    fi

    if $VARIANT_C_RUNNING; then
        echo "  ✅ Variant C (Spring MVC + Hibernate) - Port 8081"
        echo "     └─ Résultats: $RESULTS_DIR/variant-c/"
    else
        echo "  ⏭️  Variant C (Spring MVC + Hibernate) - Non testé"
    fi

    if $VARIANT_D_RUNNING; then
        echo "  ✅ Variant D (Spring Data REST) - Port 8082"
        echo "     └─ Résultats: $RESULTS_DIR/variant-d/"
    else
        echo "  ⏭️  Variant D (Spring Data REST) - Non testé"
    fi

    echo ""
    echo "═══════════════════════════════════════════════════════════════"
    echo "SCÉNARIOS EXÉCUTÉS:"
    echo ""
    echo "  1. READ-heavy    : Lecture intensive (50 threads, 600s)"
    echo "  2. JOIN-filter   : Filtrage avec JOIN (60 threads, 480s)"
    echo "  3. MIXED         : Opérations mixtes (50 threads, 600s)"
    echo "  4. HEAVY-body    : Payloads lourds (30+60 threads, 480s)"
    echo ""
    echo "═══════════════════════════════════════════════════════════════"
    echo "RAPPORTS HTML GÉNÉRÉS:"
    echo ""

    if $VARIANT_A_RUNNING; then
        echo "Variant A:"
        echo "  - $RESULTS_DIR/variant-a/*_html/index.html"
    fi

    if $VARIANT_C_RUNNING; then
        echo "Variant C:"
        echo "  - $RESULTS_DIR/variant-c/*_html/index.html"
    fi

    if $VARIANT_D_RUNNING; then
        echo "Variant D:"
        echo "  - $RESULTS_DIR/variant-d/*_html/index.html"
    fi

    echo ""
    echo "═══════════════════════════════════════════════════════════════"
    echo "PROCHAINES ÉTAPES:"
    echo ""
    echo "  1. Ouvrir les rapports HTML dans un navigateur"
    echo "  2. Consulter Grafana pour les métriques système:"
    echo "     └─ http://localhost:3003 (admin/admin123)"
    echo "  3. Consulter Prometheus pour les métriques brutes:"
    echo "     └─ http://localhost:9094"
    echo ""
    echo "═══════════════════════════════════════════════════════════════"
} > "$SUMMARY_FILE"

# Afficher le résumé
cat "$SUMMARY_FILE"

echo ""
echo -e "${CYAN}📊 Résumé complet disponible dans: $SUMMARY_FILE${NC}"
echo ""
echo -e "${GREEN}🎉 Tous les benchmarks sont terminés avec succès!${NC}"
echo ""
echo -e "${MAGENTA}💡 TIP: Comparez les résultats dans Grafana > Dashboard > Benchmark Comparison${NC}"
echo ""

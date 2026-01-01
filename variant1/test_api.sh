#!/bin/bash

# 🧪 Script de tests pour Variante A (Jersey + Hibernate)
# Auteur: Halmaoui Abdellah
# Date: Novembre 2025

echo "🧪 TESTS VARIANTE A - Jersey + Hibernate"
echo "========================================"
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

# Fonction de test
test_endpoint() {
    local method=$1
    local endpoint=$2
    local expected_status=$3
    local description=$4
    local data=$5
    
    echo -e "${BLUE}TEST:${NC} $description"
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$BASE_URL$endpoint")
    fi
    
    status_code=$(echo "$response" | tail -n 1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$status_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅ PASSED${NC} - Status: $status_code"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAILED${NC} - Expected: $expected_status, Got: $status_code"
        echo "Response: $body"
        ((FAILED++))
    fi
    echo ""
}

# Vérifier que le serveur est démarré
echo "🔍 Vérification du serveur..."
if ! curl -s "$BASE_URL/items" > /dev/null 2>&1; then
    echo -e "${RED}❌ Serveur non accessible sur $BASE_URL${NC}"
    echo "Veuillez démarrer le serveur avec ./run.sh"
    exit 1
fi
echo -e "${GREEN}✅ Serveur accessible${NC}"
echo ""

echo "=" $(printf '=%.0s' {1..50})
echo ""

# Tests pour /items
echo "📦 Tests des endpoints /items"
echo "-" $(printf '-%.0s' {1..50})

test_endpoint "GET" "/items?page=0&size=10" 200 "GET /items - Liste paginée"
test_endpoint "GET" "/items/1" 200 "GET /items/{id} - Item existant"
test_endpoint "GET" "/items/999999" 404 "GET /items/{id} - Item inexistant"
test_endpoint "GET" "/items?categoryId=1&page=0&size=5" 200 "GET /items?categoryId - Filtrage par catégorie"

# Test POST
NEW_ITEM='{
  "code": "TEST_ITEM_001",
  "name": "Test Item Jersey",
  "description": "Item créé pour tester Variante A",
  "price": 99.99,
  "stockQuantity": 50,
  "category": {"id": 1}
}'

test_endpoint "POST" "/items" 201 "POST /items - Création d'item" "$NEW_ITEM"

# Test PUT (on suppose que l'item 1 existe)
UPDATE_ITEM='{
  "code": "ITEM_1",
  "name": "Updated Item Jersey",
  "description": "Item mis à jour",
  "price": 149.99,
  "stockQuantity": 75,
  "category": {"id": 1}
}'

test_endpoint "PUT" "/items/1" 200 "PUT /items/{id} - Mise à jour d'item" "$UPDATE_ITEM"
test_endpoint "PUT" "/items/999999" 404 "PUT /items/{id} - Mise à jour d'item inexistant" "$UPDATE_ITEM"

echo ""
echo "📂 Tests des endpoints /categories"
echo "-" $(printf '-%.0s' {1..50})

test_endpoint "GET" "/categories?page=0&size=10" 200 "GET /categories - Liste paginée"
test_endpoint "GET" "/categories/1" 200 "GET /categories/{id} - Catégorie existante"
test_endpoint "GET" "/categories/999999" 404 "GET /categories/{id} - Catégorie inexistante"

# Test POST catégorie
NEW_CATEGORY='{
  "code": "TEST_CAT_001",
  "name": "Test Category Jersey"
}'

test_endpoint "POST" "/categories" 201 "POST /categories - Création de catégorie" "$NEW_CATEGORY"

# Test PUT catégorie
UPDATE_CATEGORY='{
  "code": "electronics_1",
  "name": "Updated Electronics Category"
}'

test_endpoint "PUT" "/categories/1" 200 "PUT /categories/{id} - Mise à jour de catégorie" "$UPDATE_CATEGORY"
test_endpoint "PUT" "/categories/999999" 404 "PUT /categories/{id} - Mise à jour de catégorie inexistante" "$UPDATE_CATEGORY"

echo ""
echo "🧪 Tests de performance (pagination)"
echo "-" $(printf '-%.0s' {1..50})

test_endpoint "GET" "/items?page=0&size=100" 200 "GET /items - 100 items par page"
test_endpoint "GET" "/items?page=5&size=50" 200 "GET /items - Page 5, 50 items"
test_endpoint "GET" "/categories?page=0&size=100" 200 "GET /categories - 100 catégories par page"

echo ""
echo "=" $(printf '=%.0s' {1..50})
echo ""
echo "📊 RÉSULTATS DES TESTS"
echo "   Tests réussis: ${GREEN}$PASSED${NC}"
echo "   Tests échoués: ${RED}$FAILED${NC}"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}✅ Tous les tests sont passés!${NC}"
    exit 0
else
    echo -e "${RED}❌ Certains tests ont échoué${NC}"
    exit 1
fi

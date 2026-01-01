#!/bin/bash

echo "========================================="
echo "🔍 AUDIT DE CONFIGURATION JMETER"
echo "========================================="
echo ""

cd "/Users/abderrahim_boussyf/Spring-TPs/Benchmark de performances des Web Services REST/Benchmark-performances-Web Services REST/jmeter"

echo "📁 1. VÉRIFICATION DES FICHIERS REQUIS"
echo "----------------------------------------"

FILES=("item_payload_5k.json" "item_payload_1k.json" "category_payload_1k.json" "item_ids.csv" "category_ids.csv")

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        size=$(ls -lh "$file" | awk '{print $5}')
        echo "✅ $file ($size)"
    else
        echo "❌ MANQUANT: $file"
    fi
done

echo ""
echo "📋 2. VÉRIFICATION DES CSV DATA"
echo "----------------------------------------"

if [ -f "item_ids.csv" ]; then
    echo "item_ids.csv - Premières lignes:"
    head -5 item_ids.csv
    echo "Total lignes: $(wc -l < item_ids.csv)"
fi

echo ""

if [ -f "category_ids.csv" ]; then
    echo "category_ids.csv - Premières lignes:"
    head -5 category_ids.csv
    echo "Total lignes: $(wc -l < category_ids.csv)"
fi

echo ""
echo "🌐 3. VÉRIFICATION DE L'APPLICATION"
echo "----------------------------------------"

if curl -s http://localhost:8080/items?page=0&size=1 > /dev/null 2>&1; then
    echo "✅ Application Spring Boot répond sur http://localhost:8080"
    ITEM_COUNT=$(curl -s http://localhost:8080/items?page=0\&size=1 | grep -o '"totalElements":[0-9]*' | cut -d: -f2)
    echo "   Total items dans la DB: $ITEM_COUNT"
else
    echo "❌ Application Spring Boot ne répond PAS"
    echo "   Démarrez-la avec: mvn spring-boot:run"
fi

echo ""
echo "📄 4. ANALYSE DES FICHIERS JMETER"
echo "----------------------------------------"

analyze_jmx() {
    local file=$1
    echo ""
    echo "📝 Analysing: $file"
    
    if [ ! -f "$file" ]; then
        echo "   ❌ Fichier non trouvé"
        return
    fi
    
    # Compter les samplers
    POST_COUNT=$(grep -c 'method">POST<' "$file")
    PUT_COUNT=$(grep -c 'method">PUT<' "$file")
    DELETE_COUNT=$(grep -c 'method">DELETE<' "$file")
    GET_COUNT=$(grep -c 'method">GET<' "$file")
    
    echo "   GET:    $GET_COUNT samplers"
    echo "   POST:   $POST_COUNT samplers"
    echo "   PUT:    $PUT_COUNT samplers"
    echo "   DELETE: $DELETE_COUNT samplers"
    
    # Vérifier HTTP Defaults
    if grep -q "HTTPSampler.domain\">localhost" "$file"; then
        echo "   ✅ HTTP Request Defaults configuré (localhost)"
    else
        echo "   ⚠️  HTTP Request Defaults manquant ou mal configuré"
    fi
    
    # Vérifier CSV configs
    CSV_COUNT=$(grep -c "CSVDataSet" "$file")
    echo "   CSV Data Sets: $CSV_COUNT"
    
    # Vérifier FileToString
    FILETOSTRING_COUNT=$(grep -c "FileToString" "$file")
    echo "   Références FileToString: $FILETOSTRING_COUNT"
    
    # Vérifier HeaderManager
    HEADER_COUNT=$(grep -c "HeaderManager" "$file")
    echo "   HTTP Header Managers: $HEADER_COUNT"
}

analyze_jmx "heavy_body_scenario.jmx"
analyze_jmx "mixed_scenario_fixed.jmx"
analyze_jmx "join_filter_scenario.jmx"
analyze_jmx "read_heavy_scenario.jmx"

echo ""
echo "🧪 5. TEST MANUEL DES PAYLOADS"
echo "----------------------------------------"

test_payload() {
    local method=$1
    local endpoint=$2
    local payload_file=$3
    
    if [ ! -f "$payload_file" ]; then
        echo "⚠️  $payload_file n'existe pas"
        return
    fi
    
    echo -n "Testing $method $endpoint... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" \
        -X "$method" \
        "http://localhost:8080$endpoint" \
        -H "Content-Type: application/json" \
        -d @"$payload_file" 2>&1)
    
    if [[ $response == "2"* ]] || [[ $response == "201" ]]; then
        echo "✅ HTTP $response"
    else
        echo "❌ HTTP $response"
    fi
}

if curl -s http://localhost:8080/items?page=0\&size=1 > /dev/null 2>&1; then
    test_payload "POST" "/items" "item_payload_1k.json"
    test_payload "POST" "/categories" "category_payload_1k.json"
    
    # Test PUT avec un ID existant
    EXISTING_ID=$(curl -s http://localhost:8080/items?page=0\&size=1 | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)
    if [ ! -z "$EXISTING_ID" ]; then
        test_payload "PUT" "/items/$EXISTING_ID" "item_payload_1k.json"
    fi
else
    echo "⚠️  Application non disponible, tests manuels ignorés"
fi

echo ""
echo "========================================="
echo "✅ AUDIT TERMINÉ"
echo "========================================="

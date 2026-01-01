# 📊 Récapitulatif des 4 Scénarios JMeter

## 🎯 Vue d'ensemble

| Scénario | Objectif | Threads | Durée | Payload | Fichier JMX |
|----------|----------|---------|-------|---------|-------------|
| **READ-heavy** | Lectures avec relations | 50→100→200 | 30 min (3×10min) | - | `read_heavy_scenario.jmx` |
| **JOIN-filter** | Requêtes avec JOIN | 60→120 | 16 min (2×8min) | - | `join_filter_scenario.jmx` |
| **MIXED** | Écritures sur 2 entités | 50→100 | 20 min (2×10min) | 1 KB | `mixed_scenario_fixed.jmx` |
| **HEAVY-body** | Payloads lourds | 30→60 | 16 min (2×8min) | 5 KB | `heavy_body_scenario.jmx` |

---

## 1️⃣ READ-heavy (relation incluse)

### Mix de requêtes :
- 50% GET /items?page=&size=50
- 20% GET /items?categoryId=...&page=&size=
- 20% GET /categories/{id}/items?page=&size=
- 10% GET /categories?page=&size=

### Configuration :
- **Palier 1** : 50 threads, ramp-up 60s, durée 600s (10 min)
- **Palier 2** : 100 threads, ramp-up 60s, durée 600s
- **Palier 3** : 200 threads, ramp-up 60s, durée 600s

### Objectif :
Tester les performances de **lecture** avec :
- Requêtes simples (GET /items)
- Filtres (GET /items?categoryId=...)
- Relations inverses (GET /categories/{id}/items)

### Commande :
```bash
jmeter -n -t jmeter/read_heavy_scenario.jmx \
  -l jmeter/results/variant_D_read_heavy.jtl \
  -e -o jmeter/results/variant_D_read_heavy_report
```

---

## 2️⃣ JOIN-filter ciblé

### Mix de requêtes :
- 70% GET /items?categoryId=...&page=&size=
- 30% GET /items/{id}

### Configuration :
- **Palier 1** : 60 threads, ramp-up 60s, durée 480s (8 min)
- **Palier 2** : 120 threads, ramp-up 60s, durée 480s

### Objectif :
Tester spécifiquement les performances des **JOIN** :
- Filtre par categoryId (nécessite un JOIN entre Item et Category)
- Détection du problème N+1
- Comparaison avec/sans fetch join

### Commande :
```bash
jmeter -n -t jmeter/join_filter_scenario.jmx \
  -l jmeter/results/variant_D_join_filter.jtl \
  -e -o jmeter/results/variant_D_join_filter_report
```

---

## 3️⃣ MIXED (écritures sur deux entités)

### Mix de requêtes :
- 40% GET /items?page=...
- 20% POST /items (1 KB)
- 10% PUT /items/{id} (1 KB)
- 10% DELETE /items/{id}
- 10% POST /categories (0.5–1 KB)
- 10% PUT /categories/{id}

### Configuration :
- **Palier 1** : 50 threads, ramp-up 60s, durée 600s (10 min)
- **Palier 2** : 100 threads, ramp-up 60s, durée 600s

### Objectif :
Tester un **mix réaliste** de :
- Lectures (40%)
- Écritures (60%) sur Items ET Categories
- Charge transactionnelle

### Fichiers requis :
- `item_payload_1k.json`
- `category_payload_1k.json`
- `item_ids.csv`
- `category_ids.csv`

### Commande :
```bash
jmeter -n -t jmeter/mixed_scenario_fixed.jmx \
  -l jmeter/results/variant_D_mixed.jtl \
  -e -o jmeter/results/variant_D_mixed_report
```

---

## 4️⃣ HEAVY-body (payload 5 KB)

### Mix de requêtes :
- 50% POST /items (5 KB)
- 50% PUT /items/{id} (5 KB)

### Configuration :
- **Palier 1** : 30 threads, ramp-up 60s, durée 480s (8 min)
- **Palier 2** : 60 threads, ramp-up 60s, durée 480s

### Objectif :
Tester la gestion des **payloads volumineux** :
- Sérialisation/désérialisation JSON
- Bande passante réseau
- Impact sur les performances avec gros payloads

### Fichiers requis :
- `item_payload_5k.json` (~5-7 KB)
- `item_ids.csv`

### Commandes :

**Palier 1 (30 threads) - Par défaut activé :**
```bash
jmeter -n -t jmeter/heavy_body_scenario.jmx \
  -l jmeter/results/variant_D_heavy_30.jtl \
  -e -o jmeter/results/variant_D_heavy_30_report
```

**Palier 2 (60 threads) - Modifier le fichier JMX :**
```bash
# 1. Ouvrir heavy_body_scenario.jmx dans un éditeur
# 2. Ligne 19 : enabled="false" (Palier 1)
# 3. Ligne 126 : enabled="true" (Palier 2)
# 4. Sauvegarder

jmeter -n -t jmeter/heavy_body_scenario.jmx \
  -l jmeter/results/variant_D_heavy_60.jtl \
  -e -o jmeter/results/variant_D_heavy_60_report
```

---

## 📋 Checklist avant chaque scénario

### Préparation :
- [ ] Application Spring Boot démarrée
- [ ] 100,000 items et 2,000 categories générés
- [ ] Fichiers CSV présents et valides
- [ ] Fichiers JSON payload présents
- [ ] `show-sql=false` dans application.properties

### Pendant le test :
- [ ] Surveiller les logs de l'application
- [ ] Vérifier qu'il n'y a pas d'erreurs
- [ ] Observer la consommation CPU/RAM (top/htop)

### Après le test :
- [ ] Ouvrir le rapport HTML
- [ ] Collecter les métriques pour le tableau T2
- [ ] Noter les taux d'erreur
- [ ] Identifier les endpoints les plus lents

---

## 🎯 Métriques clés à collecter

Pour chaque scénario, relever dans le rapport JMeter :

### Tableau T2 :
1. **RPS** (Throughput) : Requêtes/seconde
2. **p50** : Temps de réponse médian (Average approximatif)
3. **p95** : 95e percentile
4. **p99** : 99e percentile
5. **Err %** : Taux d'erreur

### Tableau T4 (JOIN-filter détaillé) :
Par endpoint :
- GET /items?categoryId=... → RPS, p95, Err%
- GET /items/{id} → RPS, p95, Err%

### Tableau T5 (MIXED détaillé) :
Par endpoint :
- GET /items → RPS, p95, Err%
- POST /items → RPS, p95, Err%
- PUT /items/{id} → RPS, p95, Err%
- DELETE /items/{id} → RPS, p95, Err%
- POST /categories → RPS, p95, Err%
- PUT /categories/{id} → RPS, p95, Err%

---

## 🚀 Ordre d'exécution recommandé

1. **READ-heavy** (30 min)
   - Démarre avec charge faible
   - Permet de valider le setup

2. **JOIN-filter** (16 min)
   - Test spécifique des performances de JOIN
   - Plus court

3. **MIXED Palier 1** (10 min)
   - Mix de lectures/écritures
   - Charge modérée

4. **MIXED Palier 2** (10 min)
   - Charge plus élevée
   - Détection des limites

5. **HEAVY-body Palier 1** (8 min)
   - Payloads lourds
   - 30 threads

6. **HEAVY-body Palier 2** (8 min)
   - Stress test avec payloads lourds
   - 60 threads

**Durée totale** : ~1h30

---

## 💡 Conseils

### Pour optimiser les performances :
1. Désactiver les logs SQL (`show-sql=false`)
2. Augmenter la heap JVM : `export MAVEN_OPTS="-Xms1g -Xmx2g"`
3. Configurer HikariCP correctement
4. Utiliser des index sur les colonnes `category_id`

### Pour détecter les problèmes :
1. Observer les logs pendant les tests
2. Utiliser Prometheus/Grafana si disponible
3. Analyser les requêtes SQL générées
4. Comparer avec les autres variantes (A et C)

---

## 🏆 Objectifs de performance (indicatifs)

| Scénario | RPS cible | p95 max | Err% max |
|----------|-----------|---------|----------|
| READ-heavy | > 1000 | < 100ms | < 1% |
| JOIN-filter | > 500 | < 150ms | < 1% |
| MIXED | > 300 | < 200ms | < 2% |
| HEAVY-body | > 200 | < 300ms | < 2% |

*Note : Ces valeurs dépendent de votre matériel et configuration.*


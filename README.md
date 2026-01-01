# Benchmark de Performances – Web Services REST

Profil GitHub : [https://github.com/ABDELLAH1234512345](https://github.com/ABDELLAH1234512345)

Ce projet propose une plateforme complète pour comparer les performances de trois variantes d’implémentation de services REST en Java, à l’aide de scénarios JMeter et d’un monitoring avancé (Prometheus, Grafana, PostgreSQL via Docker).

## 🚀 Objectifs
- **Comparer** différentes stacks Java pour des API REST (Jersey+Hibernate, Spring MVC, Spring Data REST)
- **Mesurer** la performance sous charge (latence, RPS, erreurs, etc.)
- **Visualiser** les résultats via Grafana
- **Automatiser** les tests et la collecte de métriques

## 🏗️ Structure du projet

```
Benchmark-de-performances-main/
├── docker-compose.yml         # Stack monitoring (Postgres, Prometheus, Grafana)
├── pom.xml                    # Dépendances Maven globales
├── run_all_benchmarks.sh      # Exécution de tous les benchmarks
├── jmeter/                    # Scénarios et payloads JMeter
├── monitoring/                # Config Prometheus & Grafana
├── src/main/java/com/example/ # Implémentation Spring (variant de base)
├── variant1/                  # Variante A: Jersey + Hibernate
├── variant2/                  # Variante C: Spring MVC + Hibernate
├── variant3/                  # Variante D: Spring Data REST
```

## 🧩 Variantes comparées
- **Variante A** : Jersey (JAX-RS) + Hibernate ([variant1/](variant1/))
- **Variante C** : Spring MVC + Hibernate ([variant2/](variant2/))
- **Variante D** : Spring Data REST ([variant3/](variant3/))

Chaque variante expose des endpoints REST pour gérer des entités `Item` et `Category`.

## ⚡ Démarrage rapide

### 1. Prérequis
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- JMeter (pour les tests de charge)

### 2. Lancer l’infrastructure de monitoring
```bash
docker-compose up -d
```
- Accès Grafana : http://localhost:3003 (admin/admin)
- Accès Prometheus : http://localhost:9094

### 3. Démarrer une variante
Exemple pour la variante A :
```bash
cd variant1
./run.sh
```
Pour les autres variantes, adaptez le dossier (`variant2`, `variant3`) et le port (8081, 8082).

### 4. Générer des données de test
Chaque variante propose un service de génération de données (catégories, items) pour simuler une base réaliste (voir scripts ou endpoints spécifiques).

### 5. Lancer les benchmarks JMeter
Depuis chaque dossier de variante :
```bash
./jmeter_tests.sh
```
Les résultats HTML sont générés dans `jmeter-results/`.

### 6. Comparer les résultats
- Les dashboards Grafana permettent de comparer RPS, latence, erreurs, etc. entre variantes.
- Les rapports JMeter HTML détaillent chaque scénario.

## 📊 Scénarios de test JMeter
- **READ-heavy** : lectures massives, pagination, relations
- **JOIN-filter** : requêtes avec jointures et filtres
- **MIXED** : écritures et lectures simultanées
- **HEAVY-body** : payloads volumineux

Voir [jmeter/SCENARIOS_SUMMARY.md](jmeter/SCENARIOS_SUMMARY.md) pour le détail des scénarios.

## 🛠️ Monitoring & Alerting
- **Prometheus** collecte les métriques de chaque service (latence, RPS, erreurs)
- **Grafana** propose un dashboard de comparaison
- **Alertes** configurées sur CPU, mémoire, disponibilité (voir `monitoring/prometheus/alerts.yml`)

## 📁 Références utiles
- [variant1/README.md](variant1/README.md) – Détails Variante A
- [jmeter/SCENARIOS_SUMMARY.md](jmeter/SCENARIOS_SUMMARY.md) – Détail des scénarios de charge
- [monitoring/grafana/dashboards/benchmark-comparison.json](monitoring/grafana/dashboards/benchmark-comparison.json) – Dashboard Grafana

## 👨‍💻 Auteur
**Halmaoui Abdellah**  
GitHub: [https://github.com/ABDELLAH1234512345](https://github.com/ABDELLAH1234512345)

---

*Pour toute question ou contribution, ouvrez une issue ou un pull request !*

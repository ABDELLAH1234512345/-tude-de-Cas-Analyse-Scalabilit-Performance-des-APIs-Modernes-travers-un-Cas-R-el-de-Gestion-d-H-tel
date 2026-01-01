# Variante A - Jersey (JAX-RS) + Hibernate

Implémentation de référence utilisant les standards Java EE/Jakarta EE.

## 🏗️ Stack Technique

- **Jersey 3.1.3** : Implémentation de référence JAX-RS
- **Hibernate 6.3.1** : ORM avec configuration native
- **Jetty 11** : Serveur embarqué
- **PostgreSQL** : Base de données
- **HikariCP** : Pool de connexions

## 📂 Structure du Projet

```
variant-a/
├── pom.xml
├── run.sh                          # Script de démarrage
├── test_api.sh                     # Tests fonctionnels
├── jmeter_tests.sh                 # Tests de performance
└── src/main/java/com/example/
    ├── config/
    │   ├── HibernateUtil.java      # Configuration Hibernate
    │   └── JerseyConfig.java       # Configuration Jersey
    ├── model/
    │   ├── Category.java           # Entité JPA
    │   └── Item.java               # Entité JPA
    ├── dao/
    │   ├── CategoryDAO.java        # Data Access Object
    │   └── ItemDAO.java            # Data Access Object
    ├── service/
    │   ├── CategoryService.java    # Logique métier
    │   ├── ItemService.java        # Logique métier
    │   └── DataGeneratorService.java
    ├── resource/
    │   ├── CategoryResource.java   # REST Controller
    │   └── ItemResource.java       # REST Controller
    └── Application.java            # Point d'entrée
```

## 🚀 Démarrage Rapide

### Prérequis

- Java 17+
- Maven 3.6+
- PostgreSQL en cours d'exécution
- Base de données `benchmark_db` (créée automatiquement si absente)

### 1. Compilation et Démarrage

```bash
cd variant-a
./run.sh
```

Le script va :
- ✅ Vérifier Java, Maven et PostgreSQL
- ✅ Créer la base de données si nécessaire
- ✅ Compiler le projet
- ✅ Générer 100K items et 2K catégories
- ✅ Démarrer le serveur sur http://localhost:8080

### 2. Tests Fonctionnels

Dans un autre terminal :

```bash
cd variant-a
./test_api.sh
```

Tests effectués :
- GET /items (pagination)
- GET /items/{id}
- GET /items?categoryId={id}
- POST /items
- PUT /items/{id}
- DELETE /items/{id}
- GET /categories (pagination)
- POST /categories
- PUT /categories/{id}

### 3. Tests de Performance (JMeter)

```bash
cd variant-a
./jmeter_tests.sh
```

Scénarios exécutés :
1. **Read-Heavy** : Lecture intensive
2. **Join Filter** : Filtrage avec JOIN
3. **Mixed Operations** : Opérations mixtes
4. **Heavy Body** : Payloads lourds

Résultats dans `variant-a/jmeter-results/`

## 📊 Endpoints API

### Items

```bash
# Liste paginée
GET http://localhost:8080/items?page=0&size=10

# Item par ID
GET http://localhost:8080/items/1

# Items par catégorie
GET http://localhost:8080/items?categoryId=5&page=0&size=10

# Créer un item
POST http://localhost:8080/items
Content-Type: application/json

{
  "code": "ITEM_NEW",
  "name": "New Item",
  "description": "Description",
  "price": 99.99,
  "stockQuantity": 100,
  "category": {"id": 1}
}

# Mettre à jour un item
PUT http://localhost:8080/items/1
Content-Type: application/json

{
  "code": "ITEM_1",
  "name": "Updated Item",
  "price": 149.99,
  "stockQuantity": 50,
  "category": {"id": 1}
}

# Supprimer un item
DELETE http://localhost:8080/items/1
```

### Categories

```bash
# Liste paginée
GET http://localhost:8080/categories?page=0&size=10

# Catégorie par ID
GET http://localhost:8080/categories/1

# Créer une catégorie
POST http://localhost:8080/categories
Content-Type: application/json

{
  "code": "NEW_CAT",
  "name": "New Category"
}

# Mettre à jour une catégorie
PUT http://localhost:8080/categories/1
Content-Type: application/json

{
  "code": "electronics_1",
  "name": "Updated Electronics"
}

# Supprimer une catégorie
DELETE http://localhost:8080/categories/1
```

## 🎯 Caractéristiques Techniques

### Pattern Architecture

- **Resource Layer** : Contrôleurs JAX-RS avec annotations `@Path`, `@GET`, `@POST`...
- **Service Layer** : Logique métier
- **DAO Layer** : Accès aux données avec gestion manuelle des transactions
- **Model Layer** : Entités JPA

### Gestion des Transactions

Transactions manuelles avec Hibernate :

```java
Transaction transaction = null;
try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    transaction = session.beginTransaction();
    // Operations
    transaction.commit();
} catch (Exception e) {
    if (transaction != null) transaction.rollback();
    throw e;
}
```

### Optimisations

1. **HikariCP** : Pool de connexions haute performance
2. **Batch Processing** : Insertion par batch de 20 (configurable)
3. **JOIN FETCH** : Évite le problème N+1 sur les relations
4. **Lazy Loading** : Chargement à la demande des relations

## 🆚 Comparaison avec Spring Boot

| Aspect | Variante A (Jersey) | Spring Boot |
|--------|---------------------|-------------|
| Configuration | Manuelle | Auto-configuration |
| DI | HK2 | Spring IoC |
| Transactions | Manuelles | `@Transactional` |
| Démarrage | ~2-3s | ~5-8s |
| Mémoire | ~200MB | ~300-400MB |
| Courbe apprentissage | Moyenne | Facile |
| Contrôle | Total | Abstrait |

## ⚙️ Configuration

### Base de données

Modifiez `src/main/java/com/example/config/HibernateUtil.java` :

```java
configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/benchmark_db");
configuration.setProperty("hibernate.connection.username", "postgres");
configuration.setProperty("hibernate.connection.password", "postgres");
```

### Pool de connexions

```java
configuration.setProperty("hibernate.hikari.minimumIdle", "5");
configuration.setProperty("hibernate.hikari.maximumPoolSize", "20");
configuration.setProperty("hibernate.hikari.idleTimeout", "300000");
```

### Port du serveur

Modifiez dans `Application.java` :

```java
Server server = new Server(8080); // Changer ici
```

## 🐛 Dépannage

### Erreur : Base de données inaccessible

```bash
# Démarrer PostgreSQL
brew services start postgresql@14

# Créer la base
createdb -U postgres benchmark_db
```

### Erreur : Port 8080 déjà utilisé

```bash
# Trouver le processus
lsof -i :8080

# Arrêter le processus
kill -9 <PID>
```

### Erreur de compilation Lombok

```bash
# Nettoyer et recompiler
mvn clean compile
```

## 📝 Logs

Les logs sont affichés sur la console. Pour modifier le niveau :

Créer `src/main/resources/simplelogger.properties` :

```properties
org.slf4j.simpleLogger.defaultLogLevel=info
org.slf4j.simpleLogger.log.org.hibernate=warn
```

## 🎓 Concepts Clés

- **JAX-RS** : Standard Java pour REST API
- **Jersey** : Implémentation de référence
- **Hibernate Native** : Sans abstractions Spring
- **DAO Pattern** : Séparation accès données
- **Jetty Embedded** : Serveur intégré

## 📚 Ressources

- [Jersey Documentation](https://eclipse-ee4j.github.io/jersey/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [JAX-RS Specification](https://jakarta.ee/specifications/restful-ws/)

---

## 👤 Auteur

- Halmaoui Abdellah — [Profil GitHub](https://github.com/ABDELLAH1234512345)

---

**Prêt à benchmarker !** 🚀

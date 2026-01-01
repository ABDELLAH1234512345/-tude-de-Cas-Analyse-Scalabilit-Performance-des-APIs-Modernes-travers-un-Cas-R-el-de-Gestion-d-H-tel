# 🅰️ GUIDE DE DÉMARRAGE RAPIDE - VARIANTE A

## ⚡ Démarrage en 3 étapes

### Étape 1 : Compiler le projet

```bash
cd variant-a
mvn clean package
```

### Étape 2 : Démarrer l'application

```bash
./run.sh
```

OU manuellement :

```bash
java -jar target/benchmark-variant-a-1.0-SNAPSHOT.jar
```

### Étape 3 : Tester

```bash
# Tests fonctionnels
./test_api.sh

# Tests de performance (optionnel)
./jmeter_tests.sh
```

## 🧪 Tests manuels avec curl

```bash
# GET items
curl http://localhost:8080/items?page=0&size=10

# GET item par ID
curl http://localhost:8080/items/1

# POST new item
curl -X POST http://localhost:8080/items \
  -H "Content-Type: application/json" \
  -d '{
    "code": "TEST001",
    "name": "Test Item",
    "description": "Test",
    "price": 99.99,
    "stockQuantity": 100,
    "category": {"id": 1}
  }'
```

## 📊 Structure des réponses

### GET /items

```json
{
  "content": [
    {
      "id": 1,
      "code": "ITEM_1",
      "name": "Product Name",
      "description": "Description",
      "price": 99.99,
      "stockQuantity": 100,
      "updatedAt": "2025-11-06T10:00:00",
      "category": {
        "id": 1,
        "code": "electronics_1",
        "name": "Electronics Category 1"
      }
    }
  ],
  "totalElements": 100000,
  "totalPages": 10000,
  "number": 0,
  "size": 10
}
```

## 🔧 Configuration

### Modifier le port (défaut: 8080)

Éditer `Application.java` ligne 22 :

```java
Server server = new Server(8080); // Changer ici
```

### Modifier la base de données

Éditer `HibernateUtil.java` lignes 17-19 :

```java
configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/benchmark_db");
configuration.setProperty("hibernate.connection.username", "postgres");
configuration.setProperty("hibernate.connection.password", "postgres");
```

## ❓ FAQ

**Q: Le serveur ne démarre pas**
- Vérifier que PostgreSQL est lancé
- Vérifier que le port 8080 est libre
- Vérifier les logs pour les erreurs

**Q: Erreur "benchmark_db does not exist"**
```bash
createdb -U postgres benchmark_db
```

**Q: Comment regénérer les données ?**
Supprimer les tables et redémarrer :
```sql
DROP TABLE item;
DROP TABLE category;
```

**Q: Les tests JMeter échouent**
- S'assurer que le serveur est démarré
- Vérifier que les fichiers CSV existent dans ../jmeter/
- Installer JMeter : `brew install jmeter`

## 🎯 Prochaines étapes

1. ✅ Démarrer Variante A
2. ✅ Lancer les tests fonctionnels
3. ✅ Lancer les tests JMeter
4. 📊 Comparer avec Variante C (Spring MVC)
5. 📊 Comparer avec Variante D (Spring Data REST)

---

## 👤 Auteur

- Halmaoui Abdellah — [Profil GitHub](https://github.com/ABDELLAH1234512345)

---

**Bon benchmark !** 🚀

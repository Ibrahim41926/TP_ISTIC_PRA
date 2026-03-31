# 🧩 Mots Croisés JavaFX - TP6 PRA

**Projet réalisé en binôme** parIbrahim balde et Ibrahima Kalil Bah

## 📋 Description du projet

Ce projet est une application de mots croisés interactive développée en Java avec JavaFX. Il s'agit du TP6 du cours de Programmation d'Application (PRA) de la L3 ISTIC.

L'application permet aux utilisateurs de jouer à des mots croisés en récupérant des grilles depuis une base de données MySQL, avec une interface graphique moderne et intuitive.

## ✨ Fonctionnalités principales

### 🎮 Jeu interactif
- **Grilles dynamiques** : Chargement aléatoire de grilles depuis la base de données de l'ISTIC
- **Navigation intuitive** : Déplacement avec les flèches directionnelles
- **Saisie facilitée** : Saisie automatique des lettres avec animation
- **Validation en temps réel** : Couleurs vertes/rouges pour les réponses correctes/incorrectes

### 🎉 Expérience utilisateur
- **Félicitations automatiques** : Boîte de dialogue de félicitations quand la grille est complétée
- **Animations fluides** : Effets visuels lors de la saisie des lettres
- **Interface moderne** : Design avec CSS personnalisé
- **Navigation par indices** : Clic sur les indices pour se positionner automatiquement

### 🛠️ Fonctionnalités techniques
- **Connexion base de données** : MySQL avec driver JDBC
- **Architecture MVC** : Séparation claire des responsabilités
- **Gestion d'erreurs** : Gestion robuste des exceptions
- **Configuration flexible** : Fichiers de configuration pour l'environnement

## 🏗️ Architecture du projet

```
src/
├── src.balde.bah.tp6/
│   ├── Main.java                 # Point d'entrée de l'application
│   ├── Crossword.java            # Modèle de la grille de mots croisés
│   ├── CrosswordController.java  # Contrôleur de l'interface
│   ├── CrosswordSquare.java      # Modèle d'une case individuelle
│   ├── CrosswordView.fxml        # Interface utilisateur (JavaFX)
│   ├── DatabaseHelper.java       # Gestionnaire de base de données
│   ├── Grid.java                 # Classe générique pour les grilles
│   ├── Clue.java                 # Modèle des indices
│   ├── Style.css                 # Feuille de style CSS
│   └── javafx.properties         # Propriétés JavaFX
```

## 🚀 Installation et exécution

### Prérequis
- **Java 21** ou supérieur
- **JavaFX 21** (SDK téléchargé séparément)
- **MySQL Connector/J** (driver JDBC)
- **Base de données MySQL** accessible

### Configuration
1. **Télécharger JavaFX 21** :
   - Aller sur [javafx.io](https://gluonhq.com/products/javafx/)
   - Télécharger le SDK JavaFX 21.x
   - Extraire dans `src/src/balde/bah/tp6/javafx-sdk-21.x/`

2. **Télécharger MySQL Connector** :
   - Aller sur [dev.mysql.com/downloads/connector/j](https://dev.mysql.com/downloads/connector/j/)
   - Télécharger `mysql-connector-j-8.x.x.jar`
   - Placer dans `lib/mysql-connector-j-8.x.x.jar`

3. **Configuration classpath** :
   - Le fichier `.classpath` est déjà configuré
   - Il référence automatiquement les bonnes librairies

### Lancement
```bash
# Depuis VS Code
F5 ou Run → Start Debugging

# Ou en ligne de commande
javac -cp "lib/*;src/src/balde/bah/tp6/javafx-sdk-21.x/lib/*" src/src/balde/bah/tp6/*.java
java -cp "lib/*;src/src/balde/bah/tp6/javafx-sdk-21.x/lib/*:src" --module-path "src/src/balde/bah/tp6/javafx-sdk-21.x/lib" --add-modules javafx.controls,javafx.fxml src.balde.bah.tp6.Main
```

## 🎯 Utilisation

### Contrôles
- **Flèches directionnelles** : Se déplacer dans la grille
- **Lettres** : Saisir des lettres (majuscules automatiques)
- **Entrée** : Valider la grille entière
- **Retour arrière** : Effacer une lettre
- **Clic souris** : Se positionner sur une case
- **Clic sur indices** : Aller directement à l'indice sélectionné

### Couleurs
- **Vert** : Lettre correcte
- **Rouge** : Lettre incorrecte
- **Noir** : Cases bloquées
- **Blanc** : Cases à remplir

## 🔧 Technologies utilisées

- **Java 21** : Langage de programmation
- **JavaFX 21** : Framework d'interface graphique
- **MySQL** : Base de données
- **JDBC** : Connexion à la base de données
- **CSS** : Stylisation de l'interface
- **FXML** : Description de l'interface utilisateur
- **Maven/Gradle** : Gestion des dépendances (optionnel)

## 📊 Fonctionnalités développées

### ✅ Implémenté
- [x] Chargement de grilles depuis base de données
- [x] Interface graphique complète avec JavaFX
- [x] Navigation clavier et souris
- [x] Validation des réponses
- [x] Animations et effets visuels
- [x] Système de félicitations
- [x] Gestion des indices horizontaux/verticaux
- [x] Architecture MVC propre
- [x] Gestion d'erreurs robuste

### 🔄 Améliorations possibles
- [ ] Mode multijoueur
- [ ] Sauvegarde de progression
- [ ] Génération automatique de grilles
- [ ] Statistiques de jeu
- [ ] Thèmes personnalisables

## 👥 Équipe de développement

- **Ibrahim Balde** : Développement backend, base de données, logique métier
- **Ibrahima Kalil Bah** : Développement frontend, interface utilisateur, design

## 📝 Notes techniques

- **Base de données** : Connexion à `mysqln.istic.univ-rennes1.fr:3306/base_xxxx`
- **Encodage** : UTF-8 pour le support des caractères spéciaux
- **Threading** : Interface JavaFX thread-safe
- **Mémoire** : Gestion optimisée des ressources graphiques

## 🎓 Contexte académique

Ce projet fait partie du cursus L3 ISTIC et démontre la maîtrise des concepts suivants :
- Programmation orientée objet avancée
- Interfaces graphiques avec JavaFX
- Connexion et requêtes base de données
- Architecture logicielle (MVC)
- Gestion d'événements et animations
- Débogage et gestion d'erreurs

---

**🎯 Projet TP6 PRA - Mots Croisés JavaFX**  
*Réalisé avec passion et rigueur académique* ✨
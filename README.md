# Potager Automatisé 

## Description
Simulation écologique avancée d'un potager, modélisant les interactions complexes entre plantes, insectes et dispositifs de traitement.

## Fonctionnalités
- Simulation dynamique de l'écosystème d'un jardin
- Gestion des cycles de vie des plantes et insectes
- Visualisation en temps réel
- Contrôle pas à pas de la simulation

## Technologies
- Spring Boot
- Java
- Thymeleaf
- JPA
- H2 Database

## Captures d'écran
### Interface Principale
![Interface Principale](/screenshots/img1.png)

### Visualisation du Potager
![Potager](/screenshots/img2.png)

## Architecture
src/
├── main/
│   ├── java/
│   │   └── com/example/potager_v1/
│   │       ├── model/
│   │       │   ├── Parcelle.java
│   │       │   ├── Plante.java
│   │       │   └── Insecte.java
│   │       ├── repository/
│   │       ├── service/
│   │       └── controller/
│   └── resources/
│       └── templates/
└── test/

## Installation
1. Clonez le repository
2. Importez dans votre IDE
3. Exécutez `PotagerV1Application.java`

## Configuration
- Fichier de configuration XML pour initialiser la simulation
- Paramètres personnalisables dans `application.properties`

## Contribution
Les contributions sont les bienvenues ! 
1. Forkez le projet
2. Créez votre branche de fonctionnalité
3. Committez vos modifications
4. Poussez et créez une Pull Request

## Licence
Licence ISIR EST SAFI

## Contact
Souifi Ismail - souifiismail@gmail.com

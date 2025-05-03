// TypeTraitement.java
package com.example.potager_v1.domain.model.enums;

public enum TypeTraitement {
    EAU(0.2, 0),       // Augmente l'humidité de 0.2, pas d'effet sur les insectes
    ENGRAIS(0.05, 0),  // Augmente légèrement l'humidité et favorise la croissance des plantes
    INSECTICIDE(0, 0.7); // Pas d'effet sur l'humidité, mais peut tuer 70% des insectes

    private final double effetHumidite;
    private final double effetInsecte;

    TypeTraitement(double effetHumidite, double effetInsecte) {
        this.effetHumidite = effetHumidite;
        this.effetInsecte = effetInsecte;
    }

    public double getEffetHumidite() {
        return effetHumidite;
    }

    public double getEffetInsecte() {
        return effetInsecte;
    }

    public static TypeTraitement fromString(String name) {
        for (TypeTraitement type : TypeTraitement.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de traitement inconnu: " + name);
    }
}
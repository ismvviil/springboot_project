

// EspeceInsecte.java
package com.example.potager_v1.model.enums;

public enum EspeceInsecte {
    PUCERON("Puceron", true),
    HANNETON("Hanneton", true),
    COCCINELLE("Coccinelle", false),
    ABEILLE("Apis mellifera", false);

    private final String nom;
    private final boolean nuisible;

    EspeceInsecte(String nom, boolean nuisible) {
        this.nom = nom;
        this.nuisible = nuisible;
    }

    public String getNom() {
        return nom;
    }

    public boolean isNuisible() {
        return nuisible;
    }

    public static EspeceInsecte fromNom(String nom) {
        for (EspeceInsecte espece : values()) {
            if (espece.getNom().equals(nom)) {
                return espece;
            }
        }
        return null;
    }
}


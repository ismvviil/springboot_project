// TypeTraitement.java
package com.example.potager_v1.model.traitement;

public enum TypeTraitement {
    EAU("Eau"),
    ENGRAIS("Engrais"),
    INSECTICIDE("Insecticide");

    private final String libelle;

    TypeTraitement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeTraitement fromLibelle(String libelle) {
        for (TypeTraitement type : values()) {
            if (type.getLibelle().equals(libelle)) {
                return type;
            }
        }
        return null;
    }
}
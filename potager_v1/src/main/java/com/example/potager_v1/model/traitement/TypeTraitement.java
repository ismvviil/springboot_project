package com.example.potager_v1.model.traitement;

/**
 * Enumération des types de traitements disponibles dans le potager
 */
public enum TypeTraitement {
    EAU("Eau"),
    ENGRAIS("Engrais"),
    INSECTICIDE("Insecticide");

    private final String libelle;

    /**
     * Constructeur
     * @param libelle Libellé du type de traitement
     */
    TypeTraitement(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Récupère le libellé du type de traitement
     * @return Le libellé
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Récupère un type de traitement à partir de son libellé
     * @param libelle Libellé du type de traitement
     * @return Le type de traitement correspondant, ou null si aucun ne correspond
     */
    public static TypeTraitement fromLibelle(String libelle) {
        for (TypeTraitement type : values()) {
            if (type.getLibelle().equals(libelle)) {
                return type;
            }
        }
        return null;
    }
}

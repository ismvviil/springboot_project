package com.example.potager_v1.model.enums;

/**
 * Enumération des espèces d'insectes disponibles dans le potager
 */
public enum EspeceInsecte {
    PUCERON("Puceron", true),
    HANNETON("Hanneton", true),
    COCCINELLE("Coccinelle", false),
    ABEILLE("Apis mellifera", false);

    private final String nom;
    private final boolean nuisible;

    /**
     * Constructeur
     * @param nom Nom de l'espèce
     * @param nuisible Indique si l'espèce est nuisible pour les plantes
     */
    EspeceInsecte(String nom, boolean nuisible) {
        this.nom = nom;
        this.nuisible = nuisible;
    }

    /**
     * Récupère le nom de l'espèce
     * @return Le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Indique si l'espèce est nuisible pour les plantes
     * @return true si l'espèce est nuisible, false sinon
     */
    public boolean isNuisible() {
        return nuisible;
    }

    /**
     * Récupère une espèce à partir de son nom
     * @param nom Nom de l'espèce
     * @return L'espèce correspondante, ou null si aucune ne correspond
     */
    public static EspeceInsecte fromNom(String nom) {
        for (EspeceInsecte espece : values()) {
            if (espece.getNom().equals(nom)) {
                return espece;
            }
        }
        return null;
    }
}
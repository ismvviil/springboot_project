package com.example.potager_v1.model.traitement;

/**
 * Classe représentant un programme de traitement dans un dispositif
 */
public class Programme {
    private TypeTraitement produit; // Type de produit (EAU, ENGRAIS, INSECTICIDE)
    private int debut;      // Pas de simulation de début
    private int duree;      // Durée d'activation en pas de simulation
    private int periode;    // Période entre deux activations

    private int compteurPeriode; // Compteur interne pour la période

    /**
     * Constructeur du programme de traitement
     * @param produit Type de produit (EAU, ENGRAIS, INSECTICIDE)
     * @param debut Pas de simulation de début
     * @param duree Durée d'activation en pas de simulation
     * @param periode Période entre deux activations
     */
    public Programme(TypeTraitement produit, int debut, int duree, int periode) {
        this.produit = produit;
        this.debut = debut;
        this.duree = duree;
        this.periode = periode;
        this.compteurPeriode = 0;
    }

    /**
     * Constructeur du programme de traitement à partir d'un libellé
     * @param produitLibelle Libellé du produit ("Eau", "Engrais", "Insecticide")
     * @param debut Pas de simulation de début
     * @param duree Durée d'activation en pas de simulation
     * @param periode Période entre deux activations
     */
    public Programme(String produitLibelle, int debut, int duree, int periode) {
        this.produit = TypeTraitement.fromLibelle(produitLibelle);
        this.debut = debut;
        this.duree = duree;
        this.periode = periode;
        this.compteurPeriode = 0;
    }

    /**
     * Vérifie si le programme est actif au pas de simulation courant
     * @param pasSimulation Pas de simulation courant
     * @return true si le programme est actif, false sinon
     */
    public boolean estActif(int pasSimulation) {
        // Si le pas de simulation est inférieur au début, le programme n'est pas actif
        if (pasSimulation < debut) {
            return false;
        }

        // Calcul du pas relatif depuis le début
        int pasRelatif = pasSimulation - debut;

        // Si le pas relatif n'est pas un multiple de la période, le programme n'est pas actif
        if (pasRelatif % periode != 0) {
            return false;
        }

        // Le programme est actif pendant la durée spécifiée
        int pasDansPeriode = pasRelatif % periode;
        return pasDansPeriode < duree;
    }

    /**
     * Getter pour le type de produit
     * @return Le type de produit (EAU, ENGRAIS, INSECTICIDE)
     */
    public TypeTraitement getProduit() {
        return produit;
    }

    /**
     * Vérifie si le programme utilise de l'eau
     * @return true si le programme utilise de l'eau, false sinon
     */
    public boolean estEau() {
        return produit == TypeTraitement.EAU;
    }

    /**
     * Vérifie si le programme utilise de l'engrais
     * @return true si le programme utilise de l'engrais, false sinon
     */
    public boolean estEngrais() {
        return produit == TypeTraitement.ENGRAIS;
    }

    /**
     * Vérifie si le programme utilise de l'insecticide
     * @return true si le programme utilise de l'insecticide, false sinon
     */
    public boolean estInsecticide() {
        return produit == TypeTraitement.INSECTICIDE;
    }

    /**
     * Getter pour le pas de simulation de début
     * @return Le pas de simulation de début
     */
    public int getDebut() {
        return debut;
    }

    /**
     * Getter pour la durée d'activation
     * @return La durée d'activation en pas de simulation
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Getter pour la période entre deux activations
     * @return La période en pas de simulation
     */
    public int getPeriode() {
        return periode;
    }
}
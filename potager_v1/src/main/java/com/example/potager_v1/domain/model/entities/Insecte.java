package com.example.potager_v1.domain.model.entities;

import com.example.potager_v1.domain.model.enums.EspeceInsecte;
import com.example.potager_v1.domain.model.enums.Sexe;

import java.util.UUID;

public class Insecte {
    private UUID id;
    private EspeceInsecte espece;
    private Sexe sexe;
    private int indiceSante;
    private int vieMax;
    private int dureeVieMax;
    private double mobilite;
    private double resistanceInsecticide;
    private int maxPortee;
    private int tempsEntreRepro;
    private int pasDeSimulationSansNourriture;
    private Parcelle parcelle;

    // Constructeur par défaut
    public Insecte() {
        this.id = UUID.randomUUID();
        this.indiceSante = 10; // Pleine santé par défaut
        this.pasDeSimulationSansNourriture = 0;
    }

    // Constructeur avec paramètres
    public Insecte(EspeceInsecte espece, Sexe sexe, int vieMax, int dureeVieMax,
                   double mobilite, double resistanceInsecticide,
                   int maxPortee, int tempsEntreRepro) {
        this();
        this.espece = espece;
        this.sexe = sexe;
        this.vieMax = vieMax;
        this.dureeVieMax = dureeVieMax;
        this.mobilite = mobilite;
        this.resistanceInsecticide = resistanceInsecticide;
        this.maxPortee = maxPortee;
        this.tempsEntreRepro = tempsEntreRepro;
    }

    // Méthodes métier
    public boolean estVivant() {
        return this.indiceSante > 0;
    }

    public void perdreVie(int points) {
        this.indiceSante -= points;
        if (this.indiceSante < 0) {
            this.indiceSante = 0;
        }
    }

    public void gagnerVie(int points) {
        this.indiceSante += points;
        if (this.indiceSante > this.vieMax) {
            this.indiceSante = this.vieMax;
        }
    }

    public boolean manger() {
        // Vérifie si l'insecte peut manger (s'il y a des plantes sur la parcelle)
        if (this.parcelle != null && !this.parcelle.getPlantes().isEmpty()) {
            // Réinitialise le compteur de pas sans nourriture
            this.pasDeSimulationSansNourriture = 0;
            return true;
        }

        // Incrémente le compteur de pas sans nourriture
        this.pasDeSimulationSansNourriture++;

        // Si l'insecte n'a pas mangé depuis trop longtemps, il perd de la vie
        if (this.pasDeSimulationSansNourriture >= 5) {
            this.perdreVie(1);
        }

        return false;
    }

    public boolean peutSurvivre(double puissanceInsecticide) {
        // Calcule la probabilité de survie basée sur la résistance de l'insecte
        double probabiliteSurvie = 1.0 - (puissanceInsecticide * (1.0 - this.resistanceInsecticide));
        return Math.random() < probabiliteSurvie;
    }

    public boolean doitSeDeplacer() {
        // Détermine si l'insecte se déplace selon sa mobilité
        return Math.random() < this.mobilite;
    }

    public boolean peutSeReproduire(Insecte autreInsecte) {
        // Vérifie les conditions de reproduction
        return this.estVivant()
                && autreInsecte.estVivant()
                && this.espece == autreInsecte.getEspece()
                && this.sexe != autreInsecte.getSexe();
    }
    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public EspeceInsecte getEspece() {
        return espece;
    }

    public void setEspece(EspeceInsecte espece) {
        this.espece = espece;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public int getIndiceSante() {
        return indiceSante;
    }

    public void setIndiceSante(int indiceSante) {
        this.indiceSante = indiceSante;
    }

    public int getVieMax() {
        return vieMax;
    }

    public void setVieMax(int vieMax) {
        this.vieMax = vieMax;
    }

    public int getDureeVieMax() {
        return dureeVieMax;
    }

    public void setDureeVieMax(int dureeVieMax) {
        this.dureeVieMax = dureeVieMax;
    }

    public double getMobilite() {
        return mobilite;
    }

    public void setMobilite(double mobilite) {
        this.mobilite = mobilite;
    }

    public double getResistanceInsecticide() {
        return resistanceInsecticide;
    }

    public void setResistanceInsecticide(double resistanceInsecticide) {
        this.resistanceInsecticide = resistanceInsecticide;
    }

    public int getMaxPortee() {
        return maxPortee;
    }

    public void setMaxPortee(int maxPortee) {
        this.maxPortee = maxPortee;
    }

    public int getTempsEntreRepro() {
        return tempsEntreRepro;
    }

    public void setTempsEntreRepro(int tempsEntreRepro) {
        this.tempsEntreRepro = tempsEntreRepro;
    }

    public int getPasDeSimulationSansNourriture() {
        return pasDeSimulationSansNourriture;
    }

    public void setPasDeSimulationSansNourriture(int pasDeSimulationSansNourriture) {
        this.pasDeSimulationSansNourriture = pasDeSimulationSansNourriture;
    }

    public Parcelle getParcelle() {
        return parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
    }
}

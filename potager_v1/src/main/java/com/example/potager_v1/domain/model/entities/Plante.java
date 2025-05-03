package com.example.potager_v1.domain.model.entities;

import com.example.potager_v1.domain.model.enums.EspecePlante;

import java.util.UUID;

public class Plante {
    private UUID id;
    private EspecePlante espece;
    private int ageActuel;
    private int ageMaturite;
    private int ageMaturiteFruit;
    private int nombreRecolteMax;
    private double surface;
    private double humiditeMin;
    private double humiditeMax;
    private Parcelle parcelle;

    ///  age f lwl 0
    public Plante() {
        this.id = UUID.randomUUID();
        this.ageActuel = 0;
    }
    // constructeur b param
    public Plante(EspecePlante espece, int ageMaturite, int ageMaturiteFruit,
                  int nombreRecolteMax, double surface,
                  double humiditeMin, double humiditeMax) {
        this.id = UUID.randomUUID();
        this.espece = espece;
        this.ageActuel = 0;
        this.ageMaturite = ageMaturite;
        this.ageMaturiteFruit = ageMaturiteFruit;
        this.nombreRecolteMax = nombreRecolteMax;
        this.surface = surface;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    // Méthodes dyal  métier
    public void vieillir() {
        this.ageActuel++;
    }
    public boolean estMature() {
        return this.ageActuel >= this.ageMaturite;
    }
    public boolean peutProduireFruits() {
        return this.ageActuel >= this.ageMaturiteFruit;
    }
    public boolean estDansConditionsOptimales() {
        if (this.parcelle == null) {
            return false;
        }

        double humidite = this.parcelle.getTauxHumidite();
        return humidite >= this.humiditeMin && humidite <= this.humiditeMax;
    }

    // Getters W Setters
    public UUID getId() {
        return id;
    }

    public EspecePlante getEspece() {
        return espece;
    }

    public void setEspece(EspecePlante espece) {
        this.espece = espece;
    }

    public int getAgeActuel() {
        return ageActuel;
    }

    public void setAgeActuel(int ageActuel) {
        this.ageActuel = ageActuel;
    }

    public int getAgeMaturite() {
        return ageMaturite;
    }

    public void setAgeMaturite(int ageMaturite) {
        this.ageMaturite = ageMaturite;
    }

    public int getAgeMaturiteFruit() {
        return ageMaturiteFruit;
    }

    public void setAgeMaturiteFruit(int ageMaturiteFruit) {
        this.ageMaturiteFruit = ageMaturiteFruit;
    }

    public int getNombreRecolteMax() {
        return nombreRecolteMax;
    }

    public void setNombreRecolteMax(int nombreRecolteMax) {
        this.nombreRecolteMax = nombreRecolteMax;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public double getHumiditeMin() {
        return humiditeMin;
    }

    public void setHumiditeMin(double humiditeMin) {
        this.humiditeMin = humiditeMin;
    }

    public double getHumiditeMax() {
        return humiditeMax;
    }

    public void setHumiditeMax(double humiditeMax) {
        this.humiditeMax = humiditeMax;
    }

    public Parcelle getParcelle() {
        return parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
    }
}

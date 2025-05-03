package com.example.potager_v1.domain.model.entities;

import com.example.potager_v1.domain.model.enums.EspecePlante;

public class PlanteDrageonnante extends Plante{
    private double probabiliteColonisation;

    // Constructeur par défaut
    public PlanteDrageonnante() {
        super();
        this.probabiliteColonisation = 0.0;
    }
    // Constructeur avec paramètres
    public PlanteDrageonnante(EspecePlante espece, int ageMaturite, int ageMaturiteFruit,
                              int nombreRecolteMax, double surface,
                              double humiditeMin, double humiditeMax,
                              double probabiliteColonisation) {
        super(espece, ageMaturite, ageMaturiteFruit, nombreRecolteMax,
                surface, humiditeMin, humiditeMax);
        this.probabiliteColonisation = probabiliteColonisation;
    }


    // Méthode spécifique aux plantes drageonnantes
    public boolean peutColoniser() {
        // La plante doit être mature pour coloniser
        if (!this.estMature()) {
            return false;
        }

        // Utiliser un générateur de nombres aléatoires pour déterminer si la colonisation a lieu
        return Math.random() < this.probabiliteColonisation;
    }

    // Getters et Setters
    public double getProbabiliteColonisation() {
        return probabiliteColonisation;
    }

    public void setProbabiliteColonisation(double probabiliteColonisation) {
        this.probabiliteColonisation = probabiliteColonisation;
    }
}

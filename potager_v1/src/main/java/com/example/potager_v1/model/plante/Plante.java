package com.example.potager_v1.model.plante;

import com.example.potager_v1.model.enums.EspecePlante;

public class Plante {
    private EspecePlante espece;
    private int age;
    private int ageMaturitePied;
    private int ageMaturiteFruit;
    private int nbRecolteMax;
    private int nbRecoltesEffectuees;
    private double surface;
    private double humiditeMin;
    private double humiditeMax;
    private boolean mature;

    /**
     * Constructeur de la plante avec enum EspecePlante
     * @param espece Espèce de la plante (enum EspecePlante)
     * @param ageMaturitePied Âge auquel la plante atteint sa maturité
     * @param ageMaturiteFruit Âge auquel la plante commence à produire des fruits
     * @param nbRecolteMax Nombre maximal de récoltes possibles
     * @param surface Surface occupée par la plante
     * @param humiditeMin Taux d'humidité minimal pour la plante
     * @param humiditeMax Taux d'humidité maximal pour la plante
     */
    public Plante(EspecePlante espece, int ageMaturitePied, int ageMaturiteFruit,
                  int nbRecolteMax, double surface, double humiditeMin, double humiditeMax) {
        this.espece = espece;
        this.ageMaturitePied = ageMaturitePied;
        this.ageMaturiteFruit = ageMaturiteFruit;
        this.nbRecolteMax = nbRecolteMax;
        this.surface = surface;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;

        // Valeurs initiales
        this.age = 0;
        this.nbRecoltesEffectuees = 0;
        this.mature = false;
    }

    /**
     * Constructeur de la plante avec nom scientifique
     * @param nomScientifique Nom scientifique de l'espèce
     * @param ageMaturitePied Âge auquel la plante atteint sa maturité
     * @param ageMaturiteFruit Âge auquel la plante commence à produire des fruits
     * @param nbRecolteMax Nombre maximal de récoltes possibles
     * @param surface Surface occupée par la plante
     * @param humiditeMin Taux d'humidité minimal pour la plante
     * @param humiditeMax Taux d'humidité maximal pour la plante
     */
    public Plante(String nomScientifique, int ageMaturitePied, int ageMaturiteFruit,
                  int nbRecolteMax, double surface, double humiditeMin, double humiditeMax) {
        this.espece = EspecePlante.fromNomScientifique(nomScientifique);
        this.ageMaturitePied = ageMaturitePied;
        this.ageMaturiteFruit = ageMaturiteFruit;
        this.nbRecolteMax = nbRecolteMax;
        this.surface = surface;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;

        // Valeurs initiales
        this.age = 0;
        this.nbRecoltesEffectuees = 0;
        this.mature = false;
    }

    /**
     * Méthode appelée à chaque pas de simulation pour mettre à jour l'état de la plante
     */
    public void miseAJour() {
        // Incrémentation de l'âge
        age++;

        // Vérification de la maturité
        if (age >= ageMaturitePied) {
            mature = true;
        }
    }

    /**
     * Vérifie si la plante peut produire des fruits
     * @return true si la plante peut produire des fruits, false sinon
     */
    public boolean peutProduireFruits() {
        return mature && age >= ageMaturiteFruit && nbRecoltesEffectuees < nbRecolteMax;
    }

    /**
     * Récolte les fruits de la plante
     * @return true si la récolte a réussi, false si la plante ne peut pas être récoltée
     */
    public boolean recolter() {
        if (peutProduireFruits()) {
            nbRecoltesEffectuees++;
            return true;
        }
        return false;
    }

    /**
     * Vérifie si le taux d'humidité est adapté à la plante
     * @param tauxHumidite Taux d'humidité à vérifier
     * @return true si le taux d'humidité est dans la plage acceptable, false sinon
     */
    public boolean humiditeAdaptee(double tauxHumidite) {
        return tauxHumidite >= humiditeMin && tauxHumidite <= humiditeMax;
    }

    /**
     * Vérifie si la plante est de type drageonnant
     * @return true si la plante est drageonnante, false sinon
     */
    public boolean estDrageonnante() {
        return espece.isDrageonnante();
    }

    // Getters et setters
    public EspecePlante getEspece() {
        return espece;
    }

    /**
     * Récupère le nom scientifique de l'espèce
     * @return Le nom scientifique
     */
    public String getNomScientifique() {
        return espece.getNomScientifique();
    }

    public int getAge() {
        return age;
    }

    public boolean isMature() {
        return mature;
    }

    public int getAgeMaturitePied() {
        return ageMaturitePied;
    }

    public int getAgeMaturiteFruit() {
        return ageMaturiteFruit;
    }

    public int getNbRecolteMax() {
        return nbRecolteMax;
    }

    public int getNbRecoltesEffectuees() {
        return nbRecoltesEffectuees;
    }

    public double getSurface() {
        return surface;
    }

    public double getHumiditeMin() {
        return humiditeMin;
    }

    public double getHumiditeMax() {
        return humiditeMax;
    }
}
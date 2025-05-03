package com.example.potager_v1.model.plante;

import com.example.potager_v1.model.enums.EspecePlante;

import java.util.Random;

/**
 * Classe représentant une plante drageonnante capable de coloniser les parcelles voisines
 */
public class PlanteDrageonnante extends Plante {
    private double probaColonisation;
    private Random random = new Random();

    /**
     * Constructeur de la plante drageonnante avec enum EspecePlante
     * @param espece Espèce de la plante (enum EspecePlante)
     * @param ageMaturitePied Âge auquel la plante atteint sa maturité
     * @param ageMaturiteFruit Âge auquel la plante commence à produire des fruits
     * @param nbRecolteMax Nombre maximal de récoltes possibles
     * @param surface Surface occupée par la plante
     * @param humiditeMin Taux d'humidité minimal pour la plante
     * @param humiditeMax Taux d'humidité maximal pour la plante
     * @param probaColonisation Probabilité de colonisation des parcelles voisines
     */
    public PlanteDrageonnante(EspecePlante espece, int ageMaturitePied, int ageMaturiteFruit,
                              int nbRecolteMax, double surface, double humiditeMin,
                              double humiditeMax, double probaColonisation) {
        super(espece, ageMaturitePied, ageMaturiteFruit, nbRecolteMax,
                surface, humiditeMin, humiditeMax);

        // Vérification que l'espèce est bien drageonnante
        if (!espece.isDrageonnante()) {
            throw new IllegalArgumentException("L'espèce " + espece.getNomScientifique() + " n'est pas drageonnante");
        }

        this.probaColonisation = probaColonisation;
    }

    /**
     * Constructeur de la plante drageonnante avec nom scientifique
     * @param nomScientifique Nom scientifique de l'espèce
     * @param ageMaturitePied Âge auquel la plante atteint sa maturité
     * @param ageMaturiteFruit Âge auquel la plante commence à produire des fruits
     * @param nbRecolteMax Nombre maximal de récoltes possibles
     * @param surface Surface occupée par la plante
     * @param humiditeMin Taux d'humidité minimal pour la plante
     * @param humiditeMax Taux d'humidité maximal pour la plante
     * @param probaColonisation Probabilité de colonisation des parcelles voisines
     */
    public PlanteDrageonnante(String nomScientifique, int ageMaturitePied, int ageMaturiteFruit,
                              int nbRecolteMax, double surface, double humiditeMin,
                              double humiditeMax, double probaColonisation) {
        super(nomScientifique, ageMaturitePied, ageMaturiteFruit, nbRecolteMax,
                surface, humiditeMin, humiditeMax);

        // Vérification que l'espèce est bien drageonnante
        if (!getEspece().isDrageonnante()) {
            throw new IllegalArgumentException("L'espèce " + nomScientifique + " n'est pas drageonnante");
        }

        this.probaColonisation = probaColonisation;
    }

    /**
     * Détermine si la plante va tenter de coloniser une parcelle voisine
     * @return true si la plante tente de coloniser, false sinon
     */
    public boolean tenterColonisation() {
        // La plante doit être mature pour coloniser
        if (!isMature()) {
            return false;
        }

        // Tirage aléatoire pour déterminer si la colonisation a lieu
        return random.nextDouble() < probaColonisation;
    }

    /**
     * Crée une nouvelle plante du même type pour la colonisation
     * @return Une nouvelle instance de plante drageonnante
     */
    public PlanteDrageonnante creerColonie() {
        return new PlanteDrageonnante(
                getEspece(),
                getAgeMaturitePied(),
                getAgeMaturiteFruit(),
                getNbRecolteMax(),
                getSurface(),
                getHumiditeMin(),
                getHumiditeMax(),
                probaColonisation
        );
    }

    /**
     * Getter pour la probabilité de colonisation
     * @return La probabilité de colonisation
     */
    public double getProbaColonisation() {
        return probaColonisation;
    }
}
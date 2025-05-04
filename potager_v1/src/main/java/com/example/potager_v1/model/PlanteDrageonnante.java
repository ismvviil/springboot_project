package com.example.potager_v1.model;

import com.example.potager_v1.model.enums.EspecePlante;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.util.Random;

@Entity
@DiscriminatorValue("DRAGEONNANTE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlanteDrageonnante extends Plante {

    @DecimalMin(value = "0.0", message = "La probabilité de colonisation doit être positive")
    @DecimalMax(value = "1.0", message = "La probabilité de colonisation ne peut pas dépasser 1.0")
    private double probaColonisation;

    // Constructeur avec les attributs essentiels
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

    // Constructeur avec nom scientifique
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

    // Logique métier spécifique
    public boolean tenterColonisation() {
        // La plante doit être mature pour coloniser
        if (!isMature()) {
            return false;
        }

        // Tirage aléatoire pour déterminer si la colonisation a lieu
        return new Random().nextDouble() < probaColonisation;
    }

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
}
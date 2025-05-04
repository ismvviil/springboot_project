package com.example.potager_v1.model;

import com.example.potager_v1.model.enums.EspecePlante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "plantes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_plante", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("SIMPLE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"parcelle"})
public class Plante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'espèce ne peut pas être nulle")
    @Enumerated(EnumType.STRING)

    private EspecePlante espece;

    @Min(value = 0, message = "L'âge ne peut pas être négatif")
    private int age = 0;

    @Min(value = 1, message = "L'âge de maturité doit être positif")
    private int ageMaturitePied;

    @Min(value = 1, message = "L'âge de maturité du fruit doit être positif")
    private int ageMaturiteFruit;

    @Min(value = 0, message = "Le nombre de récoltes doit être positif ou nul")
    private int nbRecolteMax;

    @Min(value = 0, message = "Le nombre de récoltes effectuées doit être positif ou nul")
    private int nbRecoltesEffectuees = 0;

    @DecimalMin(value = "0.0", message = "La surface doit être positive")
    private double surface;

    @DecimalMin(value = "0.0", message = "L'humidité minimale doit être positive")
    @DecimalMax(value = "1.0", message = "L'humidité maximale ne peut pas dépasser 1.0")
    private double humiditeMin;

    @DecimalMin(value = "0.0", message = "L'humidité minimale doit être positive")
    @DecimalMax(value = "1.0", message = "L'humidité maximale ne peut pas dépasser 1.0")
    private double humiditeMax;

    private boolean mature = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcelle_id")
    @JsonIgnore
    private Parcelle parcelle;

    // Constructeur avec les attributs essentiels
    public Plante(EspecePlante espece, int ageMaturitePied, int ageMaturiteFruit,
                  int nbRecolteMax, double surface, double humiditeMin, double humiditeMax) {
        this.espece = espece;
        this.ageMaturitePied = ageMaturitePied;
        this.ageMaturiteFruit = ageMaturiteFruit;
        this.nbRecolteMax = nbRecolteMax;
        this.surface = surface;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    // Constructeur avec nom scientifique
    public Plante(String nomScientifique, int ageMaturitePied, int ageMaturiteFruit,
                  int nbRecolteMax, double surface, double humiditeMin, double humiditeMax) {
        this.espece = EspecePlante.fromNomScientifique(nomScientifique);
        this.ageMaturitePied = ageMaturitePied;
        this.ageMaturiteFruit = ageMaturiteFruit;
        this.nbRecolteMax = nbRecolteMax;
        this.surface = surface;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    // Logique métier
    public void miseAJour() {
        // Incrémentation de l'âge
        age++;

        // Vérification de la maturité
        if (age >= ageMaturitePied) {
            mature = true;
        }
    }

    public boolean peutProduireFruits() {
        return mature && age >= ageMaturiteFruit && nbRecoltesEffectuees < nbRecolteMax;
    }

    public boolean recolter() {
        if (peutProduireFruits()) {
            nbRecoltesEffectuees++;
            return true;
        }
        return false;
    }

    public boolean humiditeAdaptee(double tauxHumidite) {
        return tauxHumidite >= humiditeMin && tauxHumidite <= humiditeMax;
    }

    public boolean estDrageonnante() {
        return espece != null && espece.isDrageonnante();
    }

    public String getNomScientifique() {
        return espece != null ? espece.getNomScientifique() : "Inconnue";
    }
}
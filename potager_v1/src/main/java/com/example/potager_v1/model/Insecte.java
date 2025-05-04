package com.example.potager_v1.model;

import com.example.potager_v1.model.enums.EspeceInsecte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Random;

@Entity
@Table(name = "insectes")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"parcelle"})
public class Insecte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'espèce ne peut pas être nulle")
    @Enumerated(EnumType.STRING)
    private EspeceInsecte espece;

    @Column(length = 1)
    private String sexe; // "M" ou "F"

    @Min(value = 0, message = "La vie ne peut pas être négative")
    private int vie; // Indice de bonne santé actuel

    @Min(value = 1, message = "La vie maximale doit être positive")
    private int vieMax; // Indice de bonne santé maximal

    @Min(value = 1, message = "La durée de vie maximale doit être positive")
    private int dureeVieMax; // Durée de vie maximale en pas de simulation

    @DecimalMin(value = "0.0", message = "La probabilité de mobilité doit être positive")
    @DecimalMax(value = "1.0", message = "La probabilité de mobilité ne peut pas dépasser 1.0")
    private double probaMobilite; // Probabilité de se déplacer

    @DecimalMin(value = "0.0", message = "La résistance aux insecticides doit être positive")
    @DecimalMax(value = "1.0", message = "La résistance aux insecticides ne peut pas dépasser 1.0")
    private double resistanceInsecticide; // Probabilité de survivre au traitement

    @Min(value = 1, message = "La portée maximale doit être positive")
    private int maxPortee; // Nombre maximal d'insectes lors d'une reproduction

    @Min(value = 1, message = "Le temps entre reproductions doit être positif")
    private int tempsEntreRepro; // Temps minimum entre deux reproductions

    @Min(value = 0, message = "L'âge ne peut pas être négatif")
    private int age = 0; // Âge en pas de simulation

    @Min(value = 0, message = "Le temps depuis la dernière reproduction ne peut pas être négatif")
    private int tempsDepuisDerniereRepro = 0; // Temps depuis la dernière reproduction

    @Min(value = 0, message = "Le nombre de pas sans nourriture ne peut pas être négatif")
    private int pasSansNourriture = 0; // Nombre de pas consécutifs sans nourriture

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcelle_id")
    @JsonIgnore
    private Parcelle parcelle;

    // Constructeur avec les attributs essentiels
    public Insecte(EspeceInsecte espece, String sexe, int vieMax, int dureeVieMax,
                   double probaMobilite, double resistanceInsecticide,
                   int maxPortee, int tempsEntreRepro) {
        this.espece = espece;
        this.sexe = sexe;
        this.vieMax = vieMax;
        this.vie = vieMax; // Au début, l'insecte est en pleine santé
        this.dureeVieMax = dureeVieMax;
        this.probaMobilite = probaMobilite;
        this.resistanceInsecticide = resistanceInsecticide;
        this.maxPortee = maxPortee;
        this.tempsEntreRepro = tempsEntreRepro;
    }

    // Constructeur avec nom d'espèce
    public Insecte(String especeNom, String sexe, int vieMax, int dureeVieMax,
                   double probaMobilite, double resistanceInsecticide,
                   int maxPortee, int tempsEntreRepro) {
        this.espece = EspeceInsecte.fromNom(especeNom);
        this.sexe = sexe;
        this.vieMax = vieMax;
        this.vie = vieMax; // Au début, l'insecte est en pleine santé
        this.dureeVieMax = dureeVieMax;
        this.probaMobilite = probaMobilite;
        this.resistanceInsecticide = resistanceInsecticide;
        this.maxPortee = maxPortee;
        this.tempsEntreRepro = tempsEntreRepro;
    }

    // Logique métier
    public void miseAJour() {
        // Incrémentation de l'âge
        age++;

        // Incrémentation du temps depuis la dernière reproduction
        tempsDepuisDerniereRepro++;
    }

    public void nourrir() {
        pasSansNourriture = 0;
    }

    public boolean pasNourri() {
        pasSansNourriture++;

        // Si l'insecte ne s'est pas nourri pendant 5 pas consécutifs, il meurt
        if (pasSansNourriture >= 5) {
            vie = 0;
            return true;
        }

        // Sinon, il perd un point de santé
        vie = Math.max(0, vie - 1);
        return vie <= 0;
    }

    public boolean tenterDeplacement() {
        return new Random().nextDouble() < probaMobilite;
    }

    public boolean appliquerInsecticide() {
        // Si l'insecte résiste (en fonction de sa résistance aux insecticides)
        if (new Random().nextDouble() < resistanceInsecticide) {
            return false; // L'insecte survit
        }

        // Sinon, l'insecte meurt
        vie = 0;
        return true;
    }

    public boolean peutSeReproduire() {
        return "F".equals(sexe) && tempsDepuisDerniereRepro >= tempsEntreRepro;
    }

    public int seReproduire() {
        if (!peutSeReproduire()) {
            return 0;
        }

        // Réinitialisation du compteur
        tempsDepuisDerniereRepro = 0;

        // Nombre aléatoire d'insectes créés entre 1 et maxPortee
        return 1 + new Random().nextInt(maxPortee);
    }

    public Insecte creerDescendant() {
        // Détermination aléatoire du sexe (50% de chance pour chaque sexe)
        String nouveauSexe = new Random().nextBoolean() ? "M" : "F";

        // Création d'un nouvel insecte avec les mêmes caractéristiques
        return new Insecte(
                espece,
                nouveauSexe,
                vieMax,
                dureeVieMax,
                probaMobilite,
                resistanceInsecticide,
                maxPortee,
                tempsEntreRepro
        );
    }

    public boolean estMort() {
        return vie <= 0 || age >= dureeVieMax;
    }

    public boolean estNuisible() {
        return espece != null && espece.isNuisible();
    }
}
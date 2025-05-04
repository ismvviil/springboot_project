// Programme.java
package com.example.potager_v1.model.traitement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "programmes")
@Data
@NoArgsConstructor
public class Programme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le type de produit ne peut pas être nul")
    @Enumerated(EnumType.STRING)
    private TypeTraitement produit;

    @Min(value = 0, message = "Le pas de début doit être positif ou nul")
    private int debut;

    @Min(value = 1, message = "La durée doit être positive")
    private int duree;

    @Min(value = 1, message = "La période doit être positive")
    private int periode;
    @ManyToOne
    @JoinColumn(name = "dispositif_id")
    @JsonIgnore
    private Dispositif dispositif;

    // Constructeur avec TypeTraitement
    public Programme(TypeTraitement produit, int debut, int duree, int periode) {
        this.produit = produit;
        this.debut = debut;
        this.duree = duree;
        this.periode = periode;
    }

    // Constructeur avec libellé de produit
    public Programme(String produitLibelle, int debut, int duree, int periode) {
        this.produit = TypeTraitement.fromLibelle(produitLibelle);
        this.debut = debut;
        this.duree = duree;
        this.periode = periode;
    }

    // Logique métier
    public boolean estActif(int pasSimulation) {
        if (pasSimulation < debut) {
            return false;
        }

        int pasRelatif = pasSimulation - debut;

        if (pasRelatif % periode != 0) {
            return false;
        }

        int pasDansPeriode = pasRelatif % periode;
        return pasDansPeriode < duree;
    }

    public boolean estEau() {
        return produit == TypeTraitement.EAU;
    }

    public boolean estEngrais() {
        return produit == TypeTraitement.ENGRAIS;
    }

    public boolean estInsecticide() {
        return produit == TypeTraitement.INSECTICIDE;
    }
}
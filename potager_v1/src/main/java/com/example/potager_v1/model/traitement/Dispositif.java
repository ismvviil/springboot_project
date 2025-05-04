// Dispositif.java
package com.example.potager_v1.model.traitement;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Insecte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispositifs")
@Data
@NoArgsConstructor
public class Dispositif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Le rayon doit être positif")
    private int rayon;

    @OneToOne(mappedBy = "dispositif")
    @JsonIgnore
    private Parcelle parcelle;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dispositif_id")
    private List<Programme> programmes = new ArrayList<>();

    // Constructeur avec le rayon
    public Dispositif(int rayon) {
        this.rayon = rayon;
    }

    // Méthode pour ajouter un programme
    public void ajouterProgramme(Programme programme) {
        programmes.add(programme);
    }

    // Logique métier
    public void appliquerTraitements(int pasSimulation, List<Parcelle> potager, Parcelle parcelleActuelle) {
        for (Programme programme : programmes) {
            if (programme.estActif(pasSimulation)) {
                appliquerTraitement(programme, potager, parcelleActuelle);
            }
        }
    }

    private void appliquerTraitement(Programme programme, List<Parcelle> potager, Parcelle parcelleActuelle) {
        // Récupération des parcelles dans le rayon d'action
        List<Parcelle> parcellesCibles = getParcelleDansRayon(potager, parcelleActuelle);

        // Application du traitement en fonction du type de produit
        if (programme.estEau()) {
            appliquerEau(parcellesCibles);
        } else if (programme.estEngrais()) {
            appliquerEngrais(parcellesCibles);
        } else if (programme.estInsecticide()) {
            appliquerInsecticide(parcellesCibles);
        }
    }

    private List<Parcelle> getParcelleDansRayon(List<Parcelle> potager, Parcelle parcelleActuelle) {
        List<Parcelle> parcellesCibles = new ArrayList<>();

        int x0 = parcelleActuelle.getPosX();
        int y0 = parcelleActuelle.getPosY();

        for (Parcelle parcelle : potager) {
            int x = parcelle.getPosX();
            int y = parcelle.getPosY();

            // Distance de Manhattan
            int distance = Math.abs(x - x0) + Math.abs(y - y0);

            if (distance <= rayon) {
                parcellesCibles.add(parcelle);
            }
        }

        return parcellesCibles;
    }

    private void appliquerEau(List<Parcelle> parcelles) {
        for (Parcelle parcelle : parcelles) {
            double nouveauTaux = parcelle.getTauxHumidite() + 0.2;
            parcelle.setTauxHumidite(Math.min(1.0, nouveauTaux));
        }
    }

    private void appliquerEngrais(List<Parcelle> parcelles) {
        // Effet de l'engrais à implémenter
    }

    private void appliquerInsecticide(List<Parcelle> parcelles) {
        for (Parcelle parcelle : parcelles) {
            // Create a copy of the list to iterate over
            List<Insecte> insectesActuels = new ArrayList<>(parcelle.getInsectes());

            for (Insecte insecte : insectesActuels) {
                if (insecte.appliquerInsecticide()) {
                    // Remove the insecte from the original list
                    parcelle.retirerInsecte(insecte);
                }
            }
        }
    }
}


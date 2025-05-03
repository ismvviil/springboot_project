package com.example.potager_v1.domain.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DispositifTraitement {
    private UUID id;
    private int rayonAction;
    private Parcelle parcelle;
    private List<Programme> programmes;

    // Constructeur par défaut
    public DispositifTraitement() {
        this.id = UUID.randomUUID();
        this.rayonAction = 1; // Rayon d'action par défaut (1 parcelle)
        this.programmes = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public DispositifTraitement(int rayonAction) {
        this();
        this.rayonAction = rayonAction;
    }

    // Méthodes pour gérer les programmes
    public void ajouterProgramme(Programme programme) {
        programme.setDispositif(this);
        this.programmes.add(programme);
    }
    public void retirerProgramme(Programme programme) {
        programme.setDispositif(null);
        this.programmes.remove(programme);
    }

    // Méthode pour vérifier si un traitement doit être appliqué à un pas de simulation donné
    public List<Programme> getProgrammesActifs(int pasDeSimulation) {
        List<Programme> programmesActifs = new ArrayList<>();
        for (Programme programme : this.programmes) {
            if (programme.estActif(pasDeSimulation)) {
                programmesActifs.add(programme);
            }
        }
        return programmesActifs;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public int getRayonAction() {
        return rayonAction;
    }

    public void setRayonAction(int rayonAction) {
        this.rayonAction = rayonAction;
    }

    public Parcelle getParcelle() {
        return parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }
}

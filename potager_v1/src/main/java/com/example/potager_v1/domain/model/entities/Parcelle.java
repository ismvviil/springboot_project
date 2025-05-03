package com.example.potager_v1.domain.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Parcelle {
    private UUID id;
    private int x;
    private int y;
    private double tauxHumidite;
    private List<Plante> plantes;
    private List<Insecte> insectes;
    private DispositifTraitement dispositifTraitement;

    // Constructeur par défaut
    public Parcelle() {
        this.id = UUID.randomUUID();
        this.tauxHumidite = 0.5; // Valeur d'humidité par défaut
        this.plantes = new ArrayList<>();
        this.insectes = new ArrayList<>();
    }

    // Constructeur avec coordonnées
    public Parcelle(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    // Méthodes bach ngérew les plantes
    public void ajouterPlante(Plante plante) {
        plante.setParcelle(this);
        this.plantes.add(plante);
    }

    public void retirerPlante(Plante plante) {
        plante.setParcelle(null);
        this.plantes.remove(plante);
    }

    // Méthodes pour gérer les insectes
    public void ajouterInsecte(Insecte insecte) {
        //insecte.setParcelle(this);
        this.insectes.add(insecte);
    }
    public void retirerInsecte(Insecte insecte) {
        //insecte.setParcelle(null);
        this.insectes.remove(insecte);
    }
    // Méthode pour modifier l'humidité
    public void modifierHumidite(double delta) {
        this.tauxHumidite += delta;

        // S'assurer que l'humidité reste entre 0 et 1
        if (this.tauxHumidite < 0) {
            this.tauxHumidite = 0;
        } else if (this.tauxHumidite > 1) {
            this.tauxHumidite = 1;
        }
    }
    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getTauxHumidite() {
        return tauxHumidite;
    }

    public void setTauxHumidite(double tauxHumidite) {
        this.tauxHumidite = tauxHumidite;
    }

    public List<Plante> getPlantes() {
        return plantes;
    }

    public List<Insecte> getInsectes() {
        return insectes;
    }

    public DispositifTraitement getDispositifTraitement() {
        return dispositifTraitement;
    }

    public void setDispositifTraitement(DispositifTraitement dispositifTraitement) {
        this.dispositifTraitement = dispositifTraitement;
    }
}

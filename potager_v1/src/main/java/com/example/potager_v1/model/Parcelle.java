package com.example.potager_v1.model;

import com.example.potager_v1.model.insect.Insecte;
import com.example.potager_v1.model.plante.Plante;
import com.example.potager_v1.model.traitement.Dispositif;

import java.util.ArrayList;
import java.util.List;

public class Parcelle {
    private int posX;
    private int posY;
    private double tauxHumidite = 0.5; // Valeur par défaut
  private List<Plante> plantes = new ArrayList<>();
    private List<Insecte> insectes = new ArrayList<>();
   private Dispositif dispositif;

    /**
     * Constructeur de parcelle
     * @param posX Position X de la parcelle
     * @param posY Position Y de la parcelle
     */
    public Parcelle(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    // Getters et setters
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public double getTauxHumidite() {
        return tauxHumidite;
    }

    public void setTauxHumidite(double tauxHumidite) {
        // Limiter entre 0 et 1
        this.tauxHumidite = Math.max(0, Math.min(1, tauxHumidite));
    }

    public List<Plante> getPlantes() {
        return plantes;
    }

    public void ajouterPlante(Plante plante) {
        plantes.add(plante);
    }

    public List<Insecte> getInsectes() {
        return insectes;
    }

    public void ajouterInsecte(Insecte insecte) {
        insectes.add(insecte);
    }

    public void retirerInsecte(Insecte insecte) {
        insectes.remove(insecte);
    }

    public Dispositif getDispositif() {
        return dispositif;
    }

    public void setDispositif(Dispositif dispositif) {
        this.dispositif = dispositif;
    }

    /**
     * Vérifie si la parcelle a un dispositif de traitement
     */
    public boolean aDispositif() {
        return dispositif != null;
    }

    /**
     * Méthode pour mettre à jour la parcelle à chaque pas de simulation
     */
    public void miseAJour() {
        // Mise à jour des plantes
        for (Plante plante : new ArrayList<>(plantes)) {
            plante.miseAJour();
        }

        // Mise à jour des insectes
        for (Insecte insecte : new ArrayList<>(insectes)) {
            insecte.miseAJour();
        }
    }
}

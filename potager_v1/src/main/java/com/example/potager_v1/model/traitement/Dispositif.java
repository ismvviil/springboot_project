package com.example.potager_v1.model.traitement;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.insect.Insecte;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un dispositif de traitement dans le potager
 */
public class Dispositif {
    private int rayon;  // Rayon d'action en nombre de parcelles
    private List<Programme> programmes = new ArrayList<>();

    /**
     * Constructeur du dispositif de traitement
     * @param rayon Rayon d'action en nombre de parcelles
     */
    public Dispositif(int rayon) {
        this.rayon = rayon;
    }

    /**
     * Ajoute un programme au dispositif
     * @param programme Programme à ajouter
     */
    public void ajouterProgramme(Programme programme) {
        programmes.add(programme);
    }

    /**
     * Applique les traitements actifs au pas de simulation courant
     * @param pasSimulation Pas de simulation courant
     * @param potager Liste des parcelles du potager
     * @param parcelleActuelle Parcelle sur laquelle se trouve le dispositif
     */
    public void appliquerTraitements(int pasSimulation, List<Parcelle> potager, Parcelle parcelleActuelle) {
        for (Programme programme : programmes) {
            if (programme.estActif(pasSimulation)) {
                appliquerTraitement(programme, potager, parcelleActuelle);
            }
        }
    }

    /**
     * Applique un traitement spécifique
     * @param programme Programme de traitement à appliquer
     * @param potager Liste des parcelles du potager
     * @param parcelleActuelle Parcelle sur laquelle se trouve le dispositif
     */
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

    /**
     * Récupère les parcelles dans le rayon d'action du dispositif
     * @param potager Liste des parcelles du potager
     * @param parcelleActuelle Parcelle sur laquelle se trouve le dispositif
     * @return Liste des parcelles dans le rayon d'action
     */
    private List<Parcelle> getParcelleDansRayon(List<Parcelle> potager, Parcelle parcelleActuelle) {
        List<Parcelle> parcellesCibles = new ArrayList<>();

        int x0 = parcelleActuelle.getPosX();
        int y0 = parcelleActuelle.getPosY();

        for (Parcelle parcelle : potager) {
            int x = parcelle.getPosX();
            int y = parcelle.getPosY();

            // Calcul de la distance (utilisation de la distance de Manhattan pour simplifier)
            int distance = Math.abs(x - x0) + Math.abs(y - y0);

            // Si la parcelle est dans le rayon d'action, on l'ajoute à la liste
            if (distance <= rayon) {
                parcellesCibles.add(parcelle);
            }
        }

        return parcellesCibles;
    }

    /**
     * Applique de l'eau aux parcelles cibles
     * @param parcelles Liste des parcelles cibles
     */
    private void appliquerEau(List<Parcelle> parcelles) {
        for (Parcelle parcelle : parcelles) {
            // Augmentation du taux d'humidité de 0.2 (à ajuster selon les besoins)
            double nouveauTaux = parcelle.getTauxHumidite() + 0.2;
            parcelle.setTauxHumidite(nouveauTaux);
        }
    }

    /**
     * Applique de l'engrais aux parcelles cibles
     * @param parcelles Liste des parcelles cibles
     */
    private void appliquerEngrais(List<Parcelle> parcelles) {
        // L'effet de l'engrais pourrait être implémenté dans une version plus avancée
        // Par exemple, accélérer la croissance des plantes ou augmenter la production
    }

    /**
     * Applique de l'insecticide aux parcelles cibles
     * @param parcelles Liste des parcelles cibles
     */
    private void appliquerInsecticide(List<Parcelle> parcelles) {
        for (Parcelle parcelle : parcelles) {
            List<Insecte> insectesASupprimer = new ArrayList<>();

            // Parcours des insectes pour appliquer l'insecticide
            for (Insecte insecte : parcelle.getInsectes()) {
                if (insecte.appliquerInsecticide()) {
                    // Si l'insecte meurt, on l'ajoute à la liste à supprimer
                    insectesASupprimer.add(insecte);
                }
            }

            // Suppression des insectes morts
            for (Insecte insecte : insectesASupprimer) {
                parcelle.retirerInsecte(insecte);
            }
        }
    }

    /**
     * Getter pour le rayon d'action
     * @return Le rayon d'action en nombre de parcelles
     */
    public int getRayon() {
        return rayon;
    }

    /**
     * Getter pour la liste des programmes
     * @return La liste des programmes du dispositif
     */
    public List<Programme> getProgrammes() {
        return programmes;
    }
}
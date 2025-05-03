package com.example.potager_v1;

import com.example.potager_v1.domain.model.entities.DispositifTraitement;
import com.example.potager_v1.domain.model.entities.Parcelle;
import com.example.potager_v1.domain.model.entities.Programme;
import com.example.potager_v1.domain.model.enums.TypeTraitement;

public class TestDispositifTraitement {
    public static void main(String[] args) {
        // Créer une parcelle
        Parcelle parcelle = new Parcelle(3, 4);
        System.out.println("Parcelle créée aux coordonnées (" + parcelle.getX() + ", " + parcelle.getY() + ")");

        // Créer un dispositif de traitement
        DispositifTraitement dispositif = new DispositifTraitement(2);
        parcelle.setDispositifTraitement(dispositif);
        dispositif.setParcelle(parcelle);

        // Créer des programmes
        Programme programmeEau = new Programme(TypeTraitement.EAU, 1, 2, 4);
        Programme programmeEngrais = new Programme(TypeTraitement.ENGRAIS, 1, 1, 10);
        Programme programmeInsecticide = new Programme(TypeTraitement.INSECTICIDE, 150, 1, 20);

        // Ajouter les programmes au dispositif
        dispositif.ajouterProgramme(programmeEau);
        dispositif.ajouterProgramme(programmeEngrais);
        dispositif.ajouterProgramme(programmeInsecticide);

        System.out.println("Dispositif de traitement créé avec rayon d'action " + dispositif.getRayonAction());
        System.out.println("Nombre de programmes: " + dispositif.getProgrammes().size());

        // Tester l'activation des programmes à différents pas de simulation
        int[] pasAVerifier = {0, 1, 2, 3, 4, 5, 9, 10, 11, 149, 150, 151, 170};

        for (int pas : pasAVerifier) {
            System.out.println("\nPas de simulation " + pas + ":");
            for (Programme programme : dispositif.getProgrammesActifs(pas)) {
                System.out.println("- Programme actif: " + programme.getTypeTraitement());
            }
        }
    }
}

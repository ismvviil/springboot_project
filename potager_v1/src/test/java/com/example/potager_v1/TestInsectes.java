package com.example.potager_v1;

import com.example.potager_v1.domain.model.entities.Insecte;
import com.example.potager_v1.domain.model.entities.Parcelle;
import com.example.potager_v1.domain.model.entities.Plante;
import com.example.potager_v1.domain.model.enums.EspeceInsecte;
import com.example.potager_v1.domain.model.enums.EspecePlante;
import com.example.potager_v1.domain.model.enums.Sexe;

public class TestInsectes {
    public static void main(String[] args) {
        // Créer une parcelle
        Parcelle parcelle = new Parcelle(3, 4);
        System.out.println("Parcelle créée aux coordonnées (" + parcelle.getX() + ", " + parcelle.getY() + ")");

        // Créer et ajouter une plante à la parcelle
        Plante tomate = new Plante(
                EspecePlante.TOMATE,
                60, 20, 5, 0.5, 0.5, 0.8
        );
        parcelle.ajouterPlante(tomate);

        // Créer des insectes
        Insecte puceron1 = new Insecte(
                EspeceInsecte.FOURMI.PUCERON,
                Sexe.MALE,
                15, 100, 0.65, 1.0, 10, 10
        );

        Insecte puceron2 = new Insecte(
                EspeceInsecte.PUCERON,
                Sexe.FEMELLE,
                22, 100, 0.5, 1.0, 10, 10
        );

        // Ajouter les insectes à la parcelle
        parcelle.ajouterInsecte(puceron1);
        parcelle.ajouterInsecte(puceron2);

        System.out.println("Insectes ajoutés à la parcelle: " + parcelle.getInsectes().size());

        // Tester le comportement alimentaire
        boolean aPuManger1 = puceron1.manger();
        System.out.println("Le puceron 1 a mangé: " + aPuManger1);
        System.out.println("Santé du puceron 1: " + puceron1.getIndiceSante());

        // Tester la résistance aux insecticides
        boolean survie1 = puceron1.peutSurvivre(0.7);
        System.out.println("Le puceron 1 survit à l'insecticide: " + survie1);

        // Tester la mobilité
        boolean deplacement1 = puceron1.doitSeDeplacer();
        System.out.println("Le puceron 1 veut se déplacer: " + deplacement1);

        // Tester la reproduction
        boolean peutSeReproduire = puceron1.peutSeReproduire(puceron2);
        System.out.println("Les pucerons peuvent se reproduire: " + peutSeReproduire);

        // Supprimer les plantes et tester la faim
        parcelle.retirerPlante(tomate);
        System.out.println("Plante retirée de la parcelle");

        // Simuler plusieurs pas sans nourriture
        for (int i = 0; i < 6; i++) {
            boolean aPuManger = puceron1.manger();
            System.out.println("Étape " + (i+1) + " - Le puceron a mangé: " + aPuManger
                    + ", Santé: " + puceron1.getIndiceSante()
                    + ", Pas sans nourriture: " + puceron1.getPasDeSimulationSansNourriture());
        }
    }
}

package com.example.potager_v1;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.repository.ConfigurationLoader;

import java.util.Map;

/**
 * Classe principale pour tester le chargement de la configuration
 */
public class TestPotager {

    public static void main(String[] args) {
        // Chemin vers le fichier XML à tester
        String cheminFichier = "src/main/resources/potager_test.xml";

        // Création du chargeur de configuration
        ConfigurationLoader loader = new ConfigurationLoader();

        // Chargement de la configuration
        System.out.println("Début du chargement de la configuration...");
        Map<String, Parcelle> parcelles = loader.chargerConfiguration(cheminFichier);
        System.out.println("Fin du chargement de la configuration.");

        // Affichage du nombre de parcelles chargées
        System.out.println("Nombre de parcelles chargées : " + parcelles.size());

        // Parcours et affichage des parcelles
        for (String cle : parcelles.keySet()) {
            Parcelle parcelle = parcelles.get(cle);

            System.out.println("\nParcelle (" + parcelle.getPosX() + "," + parcelle.getPosY() + "):");

            // Affichage des plantes
            System.out.println("  Plantes (" + parcelle.getPlantes().size() + "):");
            parcelle.getPlantes().forEach(plante -> {
                System.out.println("    - " + plante.getNomScientifique() +
                        " (Drageonnante: " + plante.estDrageonnante() + ")");
            });

            // Affichage des insectes
            System.out.println("  Insectes (" + parcelle.getInsectes().size() + "):");
            parcelle.getInsectes().forEach(insecte -> {
                System.out.println("    - " + insecte.getEspece().getNom() +
                        " (" + insecte.getSexe() + ", Nuisible: " +
                        insecte.estNuisible() + ")");
            });

            // Affichage du dispositif
            if (parcelle.aDispositif()) {
                System.out.println("  Dispositif (Rayon: " + parcelle.getDispositif().getRayon() + "):");
                parcelle.getDispositif().getProgrammes().forEach(programme -> {
                    System.out.println("    - Programme " + programme.getProduit().getLibelle() +
                            " (Début: " + programme.getDebut() +
                            ", Durée: " + programme.getDuree() +
                            ", Période: " + programme.getPeriode() + ")");
                });
            }
        }

        // Test d'une simulation sur quelques pas
        System.out.println("\n\nTest de simulation sur 5 pas:");
        for (int pas = 1; pas <= 5; pas++) {
            System.out.println("\nPas de simulation " + pas + ":");

            // Mise à jour des parcelles
            for (Parcelle parcelle : parcelles.values()) {
                parcelle.miseAJour();

                // Vérification de l'état des plantes
                parcelle.getPlantes().forEach(plante -> {
                    if (plante.isMature()) {
                        System.out.println("  - Plante " + plante.getNomScientifique() +
                                " en (" + parcelle.getPosX() + "," + parcelle.getPosY() +
                                ") est mature.");
                    }
                    if (plante.peutProduireFruits()) {
                        System.out.println("  - Plante " + plante.getNomScientifique() +
                                " en (" + parcelle.getPosX() + "," + parcelle.getPosY() +
                                ") peut produire des fruits.");
                    }
                });
            }
        }
    }
}
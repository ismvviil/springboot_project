package com.example.potager_v1;

import com.example.potager_v1.domain.model.entities.Parcelle;
import com.example.potager_v1.domain.model.entities.Plante;
import com.example.potager_v1.domain.model.entities.PlanteDrageonnante;
import com.example.potager_v1.domain.model.enums.EspecePlante;

public class TestPlantes {
    public static void main(String[] args) {
        // Créer une parcelle
        Parcelle parcelle = new Parcelle(1, 2);
        System.out.println("Parcelle créée aux coordonnées (" + parcelle.getX() + ", " + parcelle.getY() + ")");
        System.out.println("Taux d'humidité initial: " + parcelle.getTauxHumidite());

        // Créer une plante
        Plante tomate = new Plante(
                EspecePlante.TOMATE,
                60,   // âge de maturité
                20,   // âge de maturité des fruits
                5,    // nombre de récoltes max
                0.5,  // surface
                0.5,  // humidité min
                0.8   // humidité max
        );

        // Ajouter la plante à la parcelle
        parcelle.ajouterPlante(tomate);
        System.out.println("Plante ajoutée: " + tomate.getEspece().getNomScientifique());

        // Faire vieillir la plante
        for (int i = 0; i < 25; i++) {
            tomate.vieillir();
        }

        System.out.println("Âge de la plante après vieillissement: " + tomate.getAgeActuel());
        System.out.println("La plante est mature: " + tomate.estMature());
        System.out.println("La plante peut produire des fruits: " + tomate.peutProduireFruits());

        // Créer une plante drageonnante
        PlanteDrageonnante fraise = new PlanteDrageonnante(
                EspecePlante.FRAISE,
                20,   // âge de maturité
                15,   // âge de maturité des fruits
                10,   // nombre de récoltes max
                0.2,  // surface
                0.6,  // humidité min
                0.9,  // humidité max
                0.15  // probabilité de colonisation
        );

        // Faire vieillir la fraise
        for (int i = 0; i < 25; i++) {
            fraise.vieillir();
        }

        System.out.println("La fraise peut coloniser: " + fraise.peutColoniser());

        // Tester le changement d'humidité
        parcelle.modifierHumidite(0.3);
        System.out.println("Taux d'humidité après modification: " + parcelle.getTauxHumidite());
    }
}

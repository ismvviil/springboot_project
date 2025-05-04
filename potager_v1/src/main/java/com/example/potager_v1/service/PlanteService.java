package com.example.potager_v1.service;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Plante;
import com.example.potager_v1.model.PlanteDrageonnante;
import com.example.potager_v1.repository.PlanteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlanteService {

    private final PlanteRepository planteRepository;
    private final Random random = new Random();


    @Transactional
    public void miseAJourPlantes(List<Parcelle> parcelles) {
        log.info("Mise à jour des plantes...");
        int compteurPlantesUpdated = 0;
        int compteurPlantesMatures = 0;

        for (Parcelle parcelle : parcelles) {
            for (Plante plante : new ArrayList<>(parcelle.getPlantes())) {
                // État avant mise à jour
                boolean etaitMature = plante.isMature();
                boolean pouvaitProduireFruits = plante.peutProduireFruits();

                // Mise à jour de la plante
                plante.miseAJour();
                compteurPlantesUpdated++;

                // Vérification des changements d'état
                if (!etaitMature && plante.isMature()) {
                    compteurPlantesMatures++;
                    log.debug("Plante {} en ({},{}) est devenue mature",
                            plante.getNomScientifique(), parcelle.getPosX(), parcelle.getPosY());
                }

                if (!pouvaitProduireFruits && plante.peutProduireFruits()) {
                    log.debug("Plante {} en ({},{}) peut maintenant produire des fruits",
                            plante.getNomScientifique(), parcelle.getPosX(), parcelle.getPosY());
                }

                // Vérification de l'humidité adaptée
                if (!plante.humiditeAdaptee(parcelle.getTauxHumidite())) {
                    log.debug("Plante {} en ({},{}) : humidité non adaptée ({})",
                            plante.getNomScientifique(), parcelle.getPosX(), parcelle.getPosY(),
                            parcelle.getTauxHumidite());
                }

                // Sauvegarde de la plante mise à jour
                planteRepository.save(plante);
            }
        }

        log.info("{} plantes mises à jour, {} plantes sont devenues matures",
                compteurPlantesUpdated, compteurPlantesMatures);
    }

    /**
     * Gère la colonisation des plantes drageonnantes
     * @param parcelles Liste des parcelles
     */
    @Transactional
    public void gestionColonisation(List<Parcelle> parcelles) {
        log.info("Gestion de la colonisation par les plantes drageonnantes...");
        int compteurColonisations = 0;

        Map<Parcelle, List<PlanteDrageonnante>> nouvellesPlantes = new HashMap<>();

        for (Parcelle parcelle : parcelles) {
            for (Plante plante : parcelle.getPlantes()) {
                // Vérifier si c'est une plante drageonnante
                if (plante instanceof PlanteDrageonnante) {
                    PlanteDrageonnante planteDrag = (PlanteDrageonnante) plante;

                    // Vérifier si la plante tente de coloniser
                    if (planteDrag.tenterColonisation()) {
                        // Liste des parcelles voisines
                        List<Parcelle> parcellesVoisines = getParcellesVoisines(parcelle, parcelles);

                        if (!parcellesVoisines.isEmpty()) {
                            // Sélection aléatoire d'une parcelle voisine
                            Parcelle destination = parcellesVoisines.get(random.nextInt(parcellesVoisines.size()));

                            // Création d'une nouvelle plante pour la colonisation
                            PlanteDrageonnante nouvelleColonie = planteDrag.creerColonie();

                            // Récupérer ou créer la liste des nouvelles plantes pour cette parcelle
                            List<PlanteDrageonnante> nouvellesPlantesByParcelle = nouvellesPlantes.computeIfAbsent(
                                    destination, p -> new ArrayList<>()
                            );

                            nouvellesPlantesByParcelle.add(nouvelleColonie);
                            compteurColonisations++;

                            log.debug("Plante {} a colonisé de ({},{}) vers ({},{})",
                                    planteDrag.getNomScientifique(),
                                    parcelle.getPosX(), parcelle.getPosY(),
                                    destination.getPosX(), destination.getPosY());
                        }
                    }
                }
            }
        }

        // Ajouter les nouvelles plantes aux parcelles
        for (Map.Entry<Parcelle, List<PlanteDrageonnante>> entry : nouvellesPlantes.entrySet()) {
            Parcelle parcelle = entry.getKey();
            List<PlanteDrageonnante> plantes = entry.getValue();

            for (PlanteDrageonnante plante : plantes) {
                parcelle.ajouterPlante(plante);
                planteRepository.save(plante);
            }
        }

        log.info("{} colonisations effectuées", compteurColonisations);
    }

    /**
     * Récolte les fruits des plantes matures
     * @param parcelles Liste des parcelles
     * @return Nombre de plantes récoltées
     */
    @Transactional
    public int recolterFruits(List<Parcelle> parcelles) {
        log.info("Récolte des fruits...");
        int compteurRecoltes = 0;

        for (Parcelle parcelle : parcelles) {
            for (Plante plante : parcelle.getPlantes()) {
                if (plante.peutProduireFruits() && plante.recolter()) {
                    compteurRecoltes++;
                    log.debug("Fruits récoltés sur la plante {} en ({},{})",
                            plante.getNomScientifique(), parcelle.getPosX(), parcelle.getPosY());

                    // Sauvegarde de la plante mise à jour
                    planteRepository.save(plante);
                }
            }
        }

        log.info("{} plantes récoltées", compteurRecoltes);
        return compteurRecoltes;
    }

    /**
     * Récupère les parcelles voisines d'une parcelle donnée
     * @param parcelle Parcelle dont on veut les voisines
     * @param toutes Toutes les parcelles
     * @return Liste des parcelles voisines
     */
    public List<Parcelle> getParcellesVoisines(Parcelle parcelle, List<Parcelle> toutes) {
        int x = parcelle.getPosX();
        int y = parcelle.getPosY();

        return toutes.stream()
                .filter(p -> {
                    int dx = Math.abs(p.getPosX() - x);
                    int dy = Math.abs(p.getPosY() - y);
                    return (dx <= 1 && dy <= 1) && (dx != 0 || dy != 0);  // Exclure la parcelle elle-même
                })
                .collect(Collectors.toList());
    }

    /**
     * Compte le nombre de plantes matures pouvant produire des fruits
     * @param parcelles Liste des parcelles
     * @return Nombre de plantes matures avec fruits
     */
    public int getNbPlantesMaturesAvecFruits(List<Parcelle> parcelles) {
        int compteur = 0;

        for (Parcelle parcelle : parcelles) {
            for (Plante plante : parcelle.getPlantes()) {
                if (plante.peutProduireFruits()) {
                    compteur++;
                }
            }
        }

        return compteur;
    }

    /**
     * Compte le nombre de plantes par espèce
     * @param parcelles Liste des parcelles
     * @return Map des compteurs par espèce
     */
    public Map<String, Integer> getCompteurEspeces(List<Parcelle> parcelles) {
        Map<String, Integer> compteur = new HashMap<>();

        for (Parcelle parcelle : parcelles) {
            for (Plante plante : parcelle.getPlantes()) {
                String especeNom = plante.getNomScientifique();
                compteur.put(especeNom, compteur.getOrDefault(especeNom, 0) + 1);
            }
        }

        return compteur;
    }
}
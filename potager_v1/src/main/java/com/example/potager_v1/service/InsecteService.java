package com.example.potager_v1.service;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Insecte;
import com.example.potager_v1.repository.InsecteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InsecteService {

    private final InsecteRepository insecteRepository;
    private final Random random = new Random();

    /**
     * Gère les insectes (nutrition, déplacement, reproduction)
     * @param parcelles Liste des parcelles contenant les insectes
     */
    @Transactional
    public void gestionInsectes(List<Parcelle> parcelles) {
        log.info("Gestion des insectes...");
        int compteurInsectesUpdated = 0;
        int compteurInsectesMorts = 0;
        int compteurInsectesDeplacés = 0;
        int compteurNouveauxInsectes = 0;

        Map<Parcelle, List<Insecte>> nouveauxInsectes = new HashMap<>();

        for (Parcelle parcelle : parcelles) {
            List<Insecte> insectesASupprimer = new ArrayList<>();

            // Parcourir tous les insectes de la parcelle
            for (Insecte insecte : new ArrayList<>(parcelle.getInsectes())) {
                // Mise à jour de l'insecte
                insecte.miseAJour();
                compteurInsectesUpdated++;

                // Vérifier si l'insecte a besoin de se nourrir
                boolean aMangeUnePlante = false;
                if (insecte.estNuisible() && !parcelle.getPlantes().isEmpty()) {
                    aMangeUnePlante = true;
                    insecte.nourrir();
                }

                // Si l'insecte n'a pas mangé, il perd de la santé
                if (!aMangeUnePlante && insecte.pasNourri()) {
                    insectesASupprimer.add(insecte);
                    compteurInsectesMorts++;
                    log.debug("Insecte {} mort de faim en ({},{})",
                            insecte.getEspece().getNom(), parcelle.getPosX(), parcelle.getPosY());
                    continue;
                }

                // Vérifier si l'insecte est mort de vieillesse
                if (insecte.estMort()) {
                    insectesASupprimer.add(insecte);
                    compteurInsectesMorts++;
                    log.debug("Insecte {} mort de vieillesse en ({},{})",
                            insecte.getEspece().getNom(), parcelle.getPosX(), parcelle.getPosY());
                    continue;
                }

                // Reproduction des insectes
                if (insecte.peutSeReproduire()) {
                    int nbDescendants = insecte.seReproduire();
                    if (nbDescendants > 0) {
                        // Récupérer ou créer la liste des nouveaux insectes pour cette parcelle
                        List<Insecte> nouveauxInsectesParParcelle = nouveauxInsectes.computeIfAbsent(
                                parcelle, p -> new ArrayList<>()
                        );

                        // Création des descendants
                        for (int i = 0; i < nbDescendants; i++) {
                            Insecte descendant = insecte.creerDescendant();
                            nouveauxInsectesParParcelle.add(descendant);
                            compteurNouveauxInsectes++;
                        }

                        log.debug("Insecte {} s'est reproduit en ({},{}) : {} nouveaux insectes",
                                insecte.getEspece().getNom(), parcelle.getPosX(), parcelle.getPosY(),
                                nbDescendants);
                    }
                }

                // Déplacement des insectes
                if (insecte.tenterDeplacement()) {
                    // Liste des parcelles voisines
                    List<Parcelle> parcellesVoisines = getParcellesVoisines(parcelle, parcelles);

                    if (!parcellesVoisines.isEmpty()) {
                        // Sélection aléatoire d'une parcelle voisine
                        Parcelle destination = parcellesVoisines.get(random.nextInt(parcellesVoisines.size()));

                        // Déplacement de l'insecte vers la parcelle de destination
                        insectesASupprimer.add(insecte);

                        // Récupérer ou créer la liste des nouveaux insectes pour cette parcelle
                        List<Insecte> nouveauxInsectesParParcelle = nouveauxInsectes.computeIfAbsent(
                                destination, p -> new ArrayList<>()
                        );

                        nouveauxInsectesParParcelle.add(insecte);
                        compteurInsectesDeplacés++;

                        log.debug("Insecte {} s'est déplacé de ({},{}) vers ({},{})",
                                insecte.getEspece().getNom(), parcelle.getPosX(), parcelle.getPosY(),
                                destination.getPosX(), destination.getPosY());
                    }
                }

                // Sauvegarde de l'insecte mis à jour
                insecteRepository.save(insecte);
            }

            // Supprimer les insectes morts ou déplacés
            for (Insecte insecte : insectesASupprimer) {
                parcelle.retirerInsecte(insecte);
                // Pour les insectes morts, on les supprime de la base de données
                if (insecte.estMort()) {
                    insecteRepository.delete(insecte);
                }
            }
        }

        // Ajouter les nouveaux insectes aux parcelles
        for (Map.Entry<Parcelle, List<Insecte>> entry : nouveauxInsectes.entrySet()) {
            Parcelle parcelle = entry.getKey();
            List<Insecte> insectes = entry.getValue();

            for (Insecte insecte : insectes) {
                parcelle.ajouterInsecte(insecte);
                insecteRepository.save(insecte);
            }
        }

        log.info("{} insectes mis à jour, {} morts, {} déplacés, {} nouveaux",
                compteurInsectesUpdated, compteurInsectesMorts, compteurInsectesDeplacés, compteurNouveauxInsectes);
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

        List<Parcelle> voisines = new ArrayList<>();

        for (Parcelle p : toutes) {
            int dx = Math.abs(p.getPosX() - x);
            int dy = Math.abs(p.getPosY() - y);

            // Une parcelle est voisine si elle est adjacente (horizontalement, verticalement ou en diagonale)
            if ((dx <= 1 && dy <= 1) && (dx != 0 || dy != 0)) {  // Exclure la parcelle elle-même
                voisines.add(p);
            }
        }

        return voisines;
    }

    /**
     * Compte le nombre d'insectes par espèce
     * @param parcelles Liste des parcelles
     * @return Map des compteurs par espèce
     */
    public Map<String, Integer> getCompteurEspeces(List<Parcelle> parcelles) {
        Map<String, Integer> compteur = new HashMap<>();

        for (Parcelle parcelle : parcelles) {
            for (Insecte insecte : parcelle.getInsectes()) {
                String especeNom = insecte.getEspece().getNom();
                compteur.put(especeNom, compteur.getOrDefault(especeNom, 0) + 1);
            }
        }

        return compteur;
    }

    /**
     * Compte le nombre d'insectes nuisibles
     * @param parcelles Liste des parcelles
     * @return Nombre d'insectes nuisibles
     */
    public int getNbInsectesNuisibles(List<Parcelle> parcelles) {
        int compteur = 0;

        for (Parcelle parcelle : parcelles) {
            for (Insecte insecte : parcelle.getInsectes()) {
                if (insecte.estNuisible()) {
                    compteur++;
                }
            }
        }

        return compteur;
    }

    /**
     * Compte le nombre d'insectes par sexe
     * @param parcelles Liste des parcelles
     * @param sexe Sexe des insectes ("M" ou "F")
     * @return Nombre d'insectes du sexe spécifié
     */
    public int getNbInsectesParSexe(List<Parcelle> parcelles, String sexe) {
        int compteur = 0;

        for (Parcelle parcelle : parcelles) {
            for (Insecte insecte : parcelle.getInsectes()) {
                if (insecte.getSexe().equals(sexe)) {
                    compteur++;
                }
            }
        }

        return compteur;
    }
}
package com.example.potager_v1.service;


import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Insecte;
import com.example.potager_v1.model.traitement.Dispositif;
import com.example.potager_v1.model.traitement.Programme;
import com.example.potager_v1.model.traitement.TypeTraitement;
import com.example.potager_v1.repository.ParcelleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TraitementService {

    private final ParcelleRepository parcelleRepository;

    // Dans la classe TraitementService, méthode appliquerTraitements
    @Transactional
    public void appliquerTraitements(List<Parcelle> parcelles, int pasSimulation) {
        log.info("Application des traitements au pas {}...", pasSimulation);
        int compteurArrosages = 0;
        int compteurEngrais = 0;
        int compteurInsecticides = 0;

        // Problème ici : Parcourir une copie de la liste pour éviter ConcurrentModificationException
        // Correction : créer une nouvelle liste avant de la parcourir
        List<Parcelle> parcellesCopy = new ArrayList<>(parcelles);

        // Parcourir toutes les parcelles avec un dispositif
        for (Parcelle parcelle : parcellesCopy) {
            if (parcelle.aDispositif()) {
                Dispositif dispositif = parcelle.getDispositif();

                // Créer une copie des programmes pour éviter les problèmes de concurrence
                List<Programme> programmesCopy = new ArrayList<>(dispositif.getProgrammes());

                // Vérifier les programmes actifs
                for (Programme programme : programmesCopy) {
                    if (programme.estActif(pasSimulation)) {
                        // Récupérer les parcelles dans le rayon d'action
                        List<Parcelle> parcellesCibles = getParcelleDansRayon(parcelles, parcelle, dispositif.getRayon());

                        // Appliquer le traitement selon le type
                        if (programme.estEau()) {
                            appliquerEau(parcellesCibles);
                            compteurArrosages++;
                            log.debug("Arrosage appliqué depuis la parcelle ({},{}) sur {} parcelles",
                                    parcelle.getPosX(), parcelle.getPosY(), parcellesCibles.size());
                        } else if (programme.estEngrais()) {
                            appliquerEngrais(parcellesCibles);
                            compteurEngrais++;
                            log.debug("Engrais appliqué depuis la parcelle ({},{}) sur {} parcelles",
                                    parcelle.getPosX(), parcelle.getPosY(), parcellesCibles.size());
                        } else if (programme.estInsecticide()) {
                            int nbInsecticidesAppliques = appliquerInsecticide(parcellesCibles);
                            compteurInsecticides++;
                            log.debug("Insecticide appliqué depuis la parcelle ({},{}) sur {} parcelles, {} insectes affectés",
                                    parcelle.getPosX(), parcelle.getPosY(), parcellesCibles.size(), nbInsecticidesAppliques);
                        }
                    }
                }
            }
        }

        log.info("{} arrosages, {} engrais, {} insecticides appliqués",
                compteurArrosages, compteurEngrais, compteurInsecticides);
    }
    /**
     * Récupère les parcelles dans le rayon d'action du dispositif
     * @param toutes Toutes les parcelles
     * @param centre Parcelle centrale
     * @param rayon Rayon d'action
     * @return Liste des parcelles dans le rayon
     */
    private List<Parcelle> getParcelleDansRayon(List<Parcelle> toutes, Parcelle centre, int rayon) {
        int x0 = centre.getPosX();
        int y0 = centre.getPosY();

        return toutes.stream()
                .filter(p -> {
                    int dx = Math.abs(p.getPosX() - x0);
                    int dy = Math.abs(p.getPosY() - y0);
                    // Distance de Manhattan
                    return dx + dy <= rayon;
                })
                .collect(Collectors.toList());
    }

    /**
     * Applique de l'eau aux parcelles cibles
     * @param parcelles Liste des parcelles cibles
     */
    private void appliquerEau(List<Parcelle> parcelles) {
        for (Parcelle parcelle : parcelles) {
            // Augmentation du taux d'humidité de 0.2 (à ajuster selon les besoins)
            double nouveauTaux = Math.min(1.0, parcelle.getTauxHumidite() + 0.2);
            parcelle.setTauxHumidite(nouveauTaux);

            // Sauvegarde de la parcelle mise à jour
            parcelleRepository.save(parcelle);
        }
    }

    /**
     * Applique de l'engrais aux parcelles cibles
     * @param parcelles Liste des parcelles cibles
     */
    private void appliquerEngrais(List<Parcelle> parcelles) {
        // L'effet de l'engrais pourrait être implémenté dans une version plus avancée
        // Par exemple, accélérer la croissance des plantes ou augmenter la production

        // Dans cette implémentation simplifiée, on ne fait rien de spécial
        // Mais on pourrait ajouter un attribut "engraisActif" aux parcelles
    }

    private int appliquerInsecticide(List<Parcelle> parcelles) {
        int compteurInsectesAffectes = 0;

        for (Parcelle parcelle : parcelles) {
            // Create a copy of the list to iterate over
            List<Insecte> insectesActuels = new ArrayList<>(parcelle.getInsectes());

            // Use removeIf for a more functional approach (optional)
            compteurInsectesAffectes += insectesActuels.stream()
                    .filter(Insecte::appliquerInsecticide)
                    .map(insecte -> {
                        parcelle.retirerInsecte(insecte);
                        return 1;
                    })
                    .count();

            // Sauvegarde de la parcelle mise à jour
            parcelleRepository.save(parcelle);
        }

        return compteurInsectesAffectes;
    }

    /**
     * Vérifie si un programme est actif à un pas de simulation donné
     * @param programme Programme à vérifier
     * @param pasSimulation Pas de simulation actuel
     * @return true si le programme est actif, false sinon
     */
    public boolean isProgrammeActif(Programme programme, int pasSimulation) {
        return programme.estActif(pasSimulation);
    }

    /**
     * Crée un nouveau dispositif de traitement
     * @param rayon Rayon d'action du dispositif
     * @param parcelle Parcelle où installer le dispositif
     * @return Le dispositif créé
     */
    @Transactional
    public Dispositif creerDispositif(int rayon, Parcelle parcelle) {
        log.info("Création d'un dispositif en ({},{}) avec rayon {}",
                parcelle.getPosX(), parcelle.getPosY(), rayon);

        Dispositif dispositif = new Dispositif(rayon);
        parcelle.setDispositif(dispositif);
        dispositif.setParcelle(parcelle);

        // Sauvegarde de la parcelle mise à jour
        parcelleRepository.save(parcelle);

        return dispositif;
    }

    /**
     * Ajoute un programme à un dispositif
     * @param dispositif Dispositif auquel ajouter le programme
     * @param produit Type de produit
     * @param debut Pas de début
     * @param duree Durée d'activation
     * @param periode Période entre deux activations
     * @return Le programme créé
     */
    @Transactional
    public Programme ajouterProgramme(Dispositif dispositif, TypeTraitement produit,
                                      int debut, int duree, int periode) {
        log.info("Ajout d'un programme {} au dispositif en ({},{})",
                produit.getLibelle(), dispositif.getParcelle().getPosX(),
                dispositif.getParcelle().getPosY());

        Programme programme = new Programme(produit, debut, duree, periode);
        dispositif.ajouterProgramme(programme);

        // Sauvegarde du dispositif mis à jour
        parcelleRepository.save(dispositif.getParcelle());

        return programme;
    }
}
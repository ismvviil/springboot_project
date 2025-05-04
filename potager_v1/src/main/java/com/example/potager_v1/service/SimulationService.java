package com.example.potager_v1.service;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Simulation;
import com.example.potager_v1.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final ParcelleRepository parcelleRepository;
    private final ConfigurationLoader configurationLoader;
    private final PlanteService planteService;
    private final InsecteService insecteService;
    private final TraitementService traitementService;

    /**
     * Initialise une nouvelle simulation à partir d'un fichier de configuration
     * @param cheminFichier Chemin vers le fichier XML de configuration
     * @return La simulation créée
     */
    public Simulation initialiserSimulation(String cheminFichier) {
        log.info("Initialisation d'une nouvelle simulation à partir de '{}'", cheminFichier);

        // Charger les parcelles depuis le fichier de configuration
        Map<String, Parcelle> parcelles = configurationLoader.chargerConfiguration(cheminFichier);

        // Créer une nouvelle simulation
        Simulation simulation = new Simulation();
        simulation.setNom(configurationLoader.getNomPotager());
        simulation.setNbIterationsMax(configurationLoader.getNbIterations());
        simulation.setSizeX(configurationLoader.getSizeX());
        simulation.setSizeY(configurationLoader.getSizeY());
        simulation.setPasSimulationActuel(0);
        simulation.setEnCours(false);
        simulation.setDateCreation(new Date());

        // Sauvegarder la simulation
        simulation = simulationRepository.save(simulation);

        // Associer les parcelles à la simulation
        for (Parcelle parcelle : parcelles.values()) {
            parcelle.setSimulation(simulation);
            parcelleRepository.save(parcelle);
        }

        log.info("Simulation '{}' initialisée avec {} parcelles", simulation.getNom(), parcelles.size());
        return simulation;
    }

    /**
     * Démarre une simulation
     * @param simulationId ID de la simulation à démarrer
     * @return La simulation démarrée
     */
    public Simulation demarrerSimulation(Long simulationId) {
        log.info("Démarrage de la simulation {}", simulationId);

        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));

        simulation.setEnCours(true);
        return simulationRepository.save(simulation);
    }

    /**
     * Arrête une simulation
     * @param simulationId ID de la simulation à arrêter
     * @return La simulation arrêtée
     */
    public Simulation arreterSimulation(Long simulationId) {
        log.info("Arrêt de la simulation {}", simulationId);

        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));

        simulation.setEnCours(false);
        return simulationRepository.save(simulation);
    }

    /**
     * Exécute un pas de simulation
     * @param simulationId ID de la simulation
     * @return La simulation mise à jour
     */
    @Transactional
    public Simulation executerPas(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));

        if (!simulation.isEnCours()) {
            log.warn("Tentative d'exécuter un pas sur une simulation arrêtée: {}", simulationId);
            return simulation;
        }

        if (simulation.getPasSimulationActuel() >= simulation.getNbIterationsMax()) {
            log.info("Simulation {} terminée (nombre maximum d'itérations atteint)", simulationId);
            simulation.setEnCours(false);
            return simulationRepository.save(simulation);
        }

        // Incrémenter le pas de simulation
        simulation.setPasSimulationActuel(simulation.getPasSimulationActuel() + 1);
        log.info("Exécution du pas {} de la simulation {}", simulation.getPasSimulationActuel(), simulationId);

        // Récupérer toutes les parcelles de la simulation
        List<Parcelle> parcelles = parcelleRepository.findBySimulationId(simulationId);

        // 1. Appliquer les traitements (arrosage, insecticides, engrais)
        traitementService.appliquerTraitements(parcelles, simulation.getPasSimulationActuel());

        // 2. Mettre à jour les plantes
        planteService.miseAJourPlantes(parcelles);

        // 3. Gérer les insectes (nutrition, déplacement, reproduction)
        insecteService.gestionInsectes(parcelles);

        // 4. Gérer la colonisation des plantes drageonnantes
        planteService.gestionColonisation(parcelles);

        // Sauvegarder la simulation
        return simulationRepository.save(simulation);
    }

    /**
     * Exécute plusieurs pas de simulation
     * @param simulationId ID de la simulation
     * @param nombrePas Nombre de pas à exécuter
     * @return La simulation mise à jour
     */
    @Transactional
    public Simulation executerPlugieursPas(Long simulationId, int nombrePas) {
        Simulation simulation = null;

        for (int i = 0; i < nombrePas; i++) {
            simulation = executerPas(simulationId);
            if (!simulation.isEnCours()) {
                break;
            }
        }

        return simulation;
    }

    /**
     * Exécute la simulation jusqu'à sa fin
     * @param simulationId ID de la simulation
     * @return La simulation terminée
     */
    @Transactional
    public Simulation executerSimulationComplete(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));

        if (!simulation.isEnCours()) {
            simulation.setEnCours(true);
            simulation = simulationRepository.save(simulation);
        }

        while (simulation.isEnCours()) {
            simulation = executerPas(simulationId);
        }

        return simulation;
    }

    /**
     * Récupère l'état complet d'une simulation
     * @param simulationId ID de la simulation
     * @return Une map contenant l'état de la simulation
     */
    public Map<String, Object> getEtatSimulation(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));

        List<Parcelle> parcelles = parcelleRepository.findBySimulationId(simulationId);

        Map<String, Object> etat = new HashMap<>();
        etat.put("simulation", simulation);
        etat.put("statistiques", calculerStatistiques(parcelles));

        return etat;
    }

    /**
     * Calcule des statistiques sur l'état du potager
     * @param parcelles Liste des parcelles
     * @return Une map contenant les statistiques
     */
    private Map<String, Object> calculerStatistiques(List<Parcelle> parcelles) {
        Map<String, Object> stats = new HashMap<>();

        // Statistiques générales
        int nbParcelles = parcelles.size();
        int nbPlantes = parcelles.stream().mapToInt(p -> p.getPlantes().size()).sum();
        int nbInsectes = parcelles.stream().mapToInt(p -> p.getInsectes().size()).sum();
        int nbDispositifs = (int) parcelles.stream().filter(Parcelle::aDispositif).count();

        stats.put("nbParcelles", nbParcelles);
        stats.put("nbPlantes", nbPlantes);
        stats.put("nbInsectes", nbInsectes);
        stats.put("nbDispositifs", nbDispositifs);

        // Statistiques avancées
        int nbPlantesMaturesAvecFruits = planteService.getNbPlantesMaturesAvecFruits(parcelles);
        Map<String, Integer> compteurEspecesPlantes = planteService.getCompteurEspeces(parcelles);
        Map<String, Integer> compteurEspecesInsectes = insecteService.getCompteurEspeces(parcelles);

        stats.put("nbPlantesMaturesAvecFruits", nbPlantesMaturesAvecFruits);
        stats.put("compteurEspecesPlantes", compteurEspecesPlantes);
        stats.put("compteurEspecesInsectes", compteurEspecesInsectes);

        return stats;
    }

    /**
     * Récupère la liste de toutes les simulations
     * @return Liste des simulations
     */
    @Transactional(readOnly = true)
    public List<Simulation> getAllSimulations() {
        return simulationRepository.findAll();
    }

    /**
     * Récupère une simulation par son ID
     * @param simulationId ID de la simulation
     * @return La simulation
     */
    @Transactional(readOnly = true)
    public Simulation getSimulationById(Long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation " + simulationId + " non trouvée"));
    }

    /**
     * Supprime une simulation
     * @param simulationId ID de la simulation à supprimer
     */
    @Transactional
    public void supprimerSimulation(Long simulationId) {
        log.info("Suppression de la simulation {}", simulationId);
        simulationRepository.deleteById(simulationId);
    }
}
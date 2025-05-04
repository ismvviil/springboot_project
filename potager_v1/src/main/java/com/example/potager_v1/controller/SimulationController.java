package com.example.potager_v1.controller;

import com.example.potager_v1.dto.SimulationStateDTO;
import com.example.potager_v1.model.Simulation;
import com.example.potager_v1.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    /**
     * Récupère toutes les simulations
     * @return Liste des simulations
     */
    @GetMapping
    public ResponseEntity<List<Simulation>> getAllSimulations() {
        List<Simulation> simulations = simulationService.getAllSimulations();
        return ResponseEntity.ok(simulations);
    }

    /**
     * Récupère une simulation par son ID
     * @param id ID de la simulation
     * @return La simulation
     */
    @GetMapping("/{id}")
    public ResponseEntity<Simulation> getSimulationById(@PathVariable Long id) {
        try {
            Simulation simulation = simulationService.getSimulationById(id);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Initialise une nouvelle simulation à partir d'un fichier de configuration
     * @param file Fichier de configuration XML
     * @return La simulation créée
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Simulation> initialiserSimulation(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarder le fichier temporairement
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(tempFilePath));

            // Initialiser la simulation
            Simulation simulation = simulationService.initialiserSimulation(tempFilePath);
            return ResponseEntity.status(HttpStatus.CREATED).body(simulation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Démarre une simulation
     * @param id ID de la simulation
     * @return La simulation démarrée
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<Simulation> demarrerSimulation(@PathVariable Long id) {
        try {
            Simulation simulation = simulationService.demarrerSimulation(id);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Arrête une simulation
     * @param id ID de la simulation
     * @return La simulation arrêtée
     */
    @PostMapping("/{id}/stop")
    public ResponseEntity<Simulation> arreterSimulation(@PathVariable Long id) {
        try {
            Simulation simulation = simulationService.arreterSimulation(id);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Exécute un pas de simulation
     * @param id ID de la simulation
     * @return La simulation mise à jour
     */
    @PostMapping("/{id}/step")
    public ResponseEntity<Simulation> executerPas(@PathVariable Long id) {
        try {
            Simulation simulation = simulationService.executerPas(id);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Exécute plusieurs pas de simulation
     * @param id ID de la simulation
     * @param steps Nombre de pas à exécuter
     * @return La simulation mise à jour
     */
    @PostMapping("/{id}/steps")
    public ResponseEntity<Simulation> executerPlusieurs(@PathVariable Long id, @RequestParam int steps) {
        try {
            Simulation simulation = simulationService.executerPlugieursPas(id, steps);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Exécute la simulation jusqu'à sa fin
     * @param id ID de la simulation
     * @return La simulation terminée
     */
    @PostMapping("/{id}/run")
    public ResponseEntity<Simulation> executerSimulationComplete(@PathVariable Long id) {
        try {
            Simulation simulation = simulationService.executerSimulationComplete(id);
            return ResponseEntity.ok(simulation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère l'état complet d'une simulation
     * @param id ID de la simulation
     * @return L'état de la simulation
     */
    @GetMapping("/{id}/state")
    public ResponseEntity<SimulationStateDTO> getEtatSimulation(@PathVariable Long id) {
        try {
            Map<String, Object> etat = simulationService.getEtatSimulation(id);

            // Convertir la Map en DTO
            SimulationStateDTO dto = new SimulationStateDTO();
            dto.setSimulation((Simulation) etat.get("simulation"));
            dto.setStatistiques((Map<String, Object>) etat.get("statistiques"));

            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime une simulation
     * @param id ID de la simulation
     * @return Statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSimulation(@PathVariable Long id) {
        try {
            simulationService.supprimerSimulation(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
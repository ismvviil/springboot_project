package com.example.potager_v1.controller;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Simulation;
import com.example.potager_v1.repository.ParcelleRepository;
import com.example.potager_v1.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private SimulationService simulationService;
    @Autowired
private ParcelleRepository parcelleRepository;
    @GetMapping("/")
    public String index(Model model) {
        List<Simulation> simulations = simulationService.getAllSimulations();
        model.addAttribute("simulations", simulations);
        return "index";
    }

    @GetMapping("/simulation/{id}")
    public String viewSimulation(@PathVariable Long id, Model model) {
        Simulation simulation = simulationService.getSimulationById(id);
        Map<String, Object> etat = simulationService.getEtatSimulation(id);

        // Récupérer les parcelles de la simulation
        List<Parcelle> parcelles = parcelleRepository.findBySimulationId(id);

        // Créer une matrice pour représenter le potager
        Parcelle[][] potager = new Parcelle[simulation.getSizeY()][simulation.getSizeX()];

        // Remplir la matrice avec les parcelles
        for (Parcelle parcelle : parcelles) {
            if (parcelle.getPosX() < simulation.getSizeX() &&
                    parcelle.getPosY() < simulation.getSizeY()) {
                potager[parcelle.getPosY()][parcelle.getPosX()] = parcelle;
            }
        }

        model.addAttribute("simulation", simulation);
        model.addAttribute("statistiques", etat.get("statistiques"));
        model.addAttribute("potager", potager);
        return "simulation";
    }

    @PostMapping("/simulation/new")
    public String createSimulation(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarder le fichier temporairement
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(tempFilePath));

            // Initialiser la simulation
            Simulation simulation = simulationService.initialiserSimulation(tempFilePath);
            return "redirect:/simulation/" + simulation.getId();
        } catch (Exception e) {
            return "redirect:/?error=" + e.getMessage();
        }
    }

    @PostMapping("/simulation/{id}/start")
    public String startSimulation(@PathVariable Long id) {
        simulationService.demarrerSimulation(id);
        return "redirect:/simulation/" + id;
    }

    @PostMapping("/simulation/{id}/step")
    public String stepSimulation(@PathVariable Long id) {
        simulationService.executerPas(id);
        return "redirect:/simulation/" + id;
    }

    @PostMapping("/simulation/{id}/stop")
    public String stopSimulation(@PathVariable Long id) {
        simulationService.arreterSimulation(id);
        return "redirect:/simulation/" + id;
    }
}
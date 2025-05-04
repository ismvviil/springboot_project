package com.example.potager_v1.controller;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Insecte;
import com.example.potager_v1.model.Plante;
import com.example.potager_v1.repository.ParcelleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcelles")
@RequiredArgsConstructor
public class ParcelleController {

    private final ParcelleRepository parcelleRepository;

    /**
     * Récupère toutes les parcelles
     * @return Liste des parcelles
     */
    @GetMapping
    public ResponseEntity<List<Parcelle>> getAllParcelles() {
        List<Parcelle> parcelles = parcelleRepository.findAll();
        return ResponseEntity.ok(parcelles);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Parcelle> getParcelleById(@PathVariable Long id) {
        Optional<Parcelle> parcelle = parcelleRepository.findById(id);
        return parcelle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/position")
    public ResponseEntity<Parcelle> getParcelleByPosition(@RequestParam int x, @RequestParam int y) {
        Optional<Parcelle> parcelle = parcelleRepository.findByPosXAndPosY(x, y);
        return parcelle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/simulation/{simulationId}")
    public ResponseEntity<List<Parcelle>> getParcellesBySimulation(@PathVariable Long simulationId) {
        List<Parcelle> parcelles = parcelleRepository.findBySimulationId(simulationId);
        return ResponseEntity.ok(parcelles);
    }


    @GetMapping("/{id}/plantes")
    public ResponseEntity<List<Plante>> getPlantesByParcelle(@PathVariable Long id) {
        Optional<Parcelle> optParcelle = parcelleRepository.findById(id);
        if (optParcelle.isPresent()) {
            return ResponseEntity.ok(optParcelle.get().getPlantes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/insectes")
    public ResponseEntity<List<Insecte>> getInsectesByParcelle(@PathVariable Long id) {
        Optional<Parcelle> optParcelle = parcelleRepository.findById(id);
        if (optParcelle.isPresent()) {
            return ResponseEntity.ok(optParcelle.get().getInsectes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PatchMapping("/{id}/humidite")
    public ResponseEntity<Parcelle> updateTauxHumidite(@PathVariable Long id, @RequestParam double tauxHumidite) {
        Optional<Parcelle> optParcelle = parcelleRepository.findById(id);
        if (optParcelle.isPresent()) {
            Parcelle parcelle = optParcelle.get();
            parcelle.setTauxHumidite(Math.max(0, Math.min(1, tauxHumidite)));
            return ResponseEntity.ok(parcelleRepository.save(parcelle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/voisines")
    public ResponseEntity<List<Parcelle>> getParcellesVoisines(@PathVariable Long id) {
        Optional<Parcelle> optParcelle = parcelleRepository.findById(id);
        if (optParcelle.isPresent()) {
            Parcelle parcelle = optParcelle.get();
            List<Parcelle> voisines = parcelleRepository.findVoisines(parcelle.getPosX(), parcelle.getPosY());
            return ResponseEntity.ok(voisines);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.example.potager_v1.model;

import com.example.potager_v1.model.traitement.Dispositif;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

@Table(name = "parcelles", indexes = {
        @Index(name = "idx_position", columnList = "posX,posY,simulation_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"plantes", "insectes", "dispositif"})
public class Parcelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int posX;

    @Column(nullable = false)
    private int posY;

    @Column(name = "taux_humidite")
    private double tauxHumidite = 0.5;

    @OneToMany(mappedBy = "parcelle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plante> plantes = new ArrayList<>();

    @OneToMany(mappedBy = "parcelle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Insecte> insectes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dispositif_id")
    private Dispositif dispositif;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date derniereMiseAJour;

    // Ajout de la relation avec Simulation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
    // Constructeur avec les attributs essentiels
    public Parcelle(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.tauxHumidite = 0.5;
    }

    // Méthodes pour gérer les relations
    public void ajouterPlante(Plante plante) {
        plantes.add(plante);
        plante.setParcelle(this);
    }

    public void ajouterInsecte(Insecte insecte) {
        insectes.add(insecte);
        insecte.setParcelle(this);
    }

    public void retirerInsecte(Insecte insecte) {
        insectes.remove(insecte);
        insecte.setParcelle(null);
    }

    public boolean aDispositif() {
        return dispositif != null;
    }

    // Logique métier pour la mise à jour
    public void miseAJour() {
        this.derniereMiseAJour = new java.util.Date();

        // Mise à jour de toutes les plantes
        for (Plante plante : new ArrayList<>(plantes)) {
            plante.miseAJour();
        }

        // Mise à jour de tous les insectes
        for (Insecte insecte : new ArrayList<>(insectes)) {
            insecte.miseAJour();
        }
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.derniereMiseAJour = new java.util.Date();
    }
}
package com.example.potager_v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "simulations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "nb_iterations_max")
    private int nbIterationsMax;

    @Column(name = "pas_simulation_actuel")
    private int pasSimulationActuel;

    @Column(name = "size_x")
    private int sizeX;

    @Column(name = "size_y")
    private int sizeY;

    @Column(name = "en_cours")
    private boolean enCours;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation")
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_derniere_execution")
    private Date dateDerniereExecution;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Parcelle> parcelles = new ArrayList<>();



    public boolean estTerminee() {
        return pasSimulationActuel >= nbIterationsMax;
    }

    /**
     * Calcule le pourcentage de progression de la simulation
     * @return Le pourcentage de progression
     */
    public double getProgression() {
        if (nbIterationsMax == 0) {
            return 0;
        }
        return (double) pasSimulationActuel / nbIterationsMax * 100;
    }

    /**
     * Récupère la durée d'exécution en millisecondes
     * @return La durée d'exécution ou 0 si la simulation n'a pas été exécutée
     */
    public long getDureeExecution() {
        if (dateCreation == null || dateDerniereExecution == null) {
            return 0;
        }
        return dateDerniereExecution.getTime() - dateCreation.getTime();
    }

    /**
     * Méthode appelée avant la mise à jour
     */
    @PreUpdate
    protected void onUpdate() {
        this.dateDerniereExecution = new Date();
    }

    /**
     * Méthode appelée avant la création
     */
    @PrePersist
    protected void onCreate() {
        if (this.dateCreation == null) {
            this.dateCreation = new Date();
        }
        this.dateDerniereExecution = new Date();
    }
}
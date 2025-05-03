package com.example.potager_v1.domain.model.entities;

import com.example.potager_v1.domain.model.enums.TypeTraitement;

import java.util.UUID;

public class Programme {
    private UUID id;
    private int instantDebut;
    private int duree;
    private int periode;
    private TypeTraitement typeTraitement;
    private DispositifTraitement dispositif;

    // Constructeur par défaut
    public Programme() {
        this.id = UUID.randomUUID();
    }
    // Constructeur avec paramètres
    public Programme(TypeTraitement typeTraitement, int instantDebut, int duree, int periode) {
        this();
        this.typeTraitement = typeTraitement;
        this.instantDebut = instantDebut;
        this.duree = duree;
        this.periode = periode;
    }

    // Méthode pour déterminer si le programme est actif à un pas de simulation donné
    public boolean estActif(int pasDeSimulation) {
        // Vérifier si nous sommes après l'instant de début
        if (pasDeSimulation < instantDebut) {
            return false;
        }

        // Calculer le nombre de pas depuis le début
        int pasDepuisDebut = pasDeSimulation - instantDebut;

        // Si la période est 0, le programme ne s'exécute qu'une fois
        if (periode == 0) {
            return pasDepuisDebut < duree;
        }

        // Sinon, vérifier si nous sommes dans une période active
        int positionDansPeriode = pasDepuisDebut % periode;
        return positionDansPeriode < duree;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public int getInstantDebut() {
        return instantDebut;
    }

    public void setInstantDebut(int instantDebut) {
        this.instantDebut = instantDebut;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    public TypeTraitement getTypeTraitement() {
        return typeTraitement;
    }

    public void setTypeTraitement(TypeTraitement typeTraitement) {
        this.typeTraitement = typeTraitement;
    }

    public DispositifTraitement getDispositif() {
        return dispositif;
    }

    public void setDispositif(DispositifTraitement dispositif) {
        this.dispositif = dispositif;
    }
}

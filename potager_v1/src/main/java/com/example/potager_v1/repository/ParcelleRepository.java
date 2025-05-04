package com.example.potager_v1.repository;

import com.example.potager_v1.model.Parcelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {

    /**
     * Trouve une parcelle par ses coordonnées
     * @param posX Position X
     * @param posY Position Y
     * @return La parcelle correspondante (optionnelle)
     */
    Optional<Parcelle> findByPosXAndPosY(int posX, int posY);

    /**
     * Trouve toutes les parcelles dans une zone rectangulaire
     * @param minX Coordonnée X minimale
     * @param maxX Coordonnée X maximale
     * @param minY Coordonnée Y minimale
     * @param maxY Coordonnée Y maximale
     * @return Liste des parcelles dans la zone
     */
    List<Parcelle> findByPosXBetweenAndPosYBetween(int minX, int maxX, int minY, int maxY);

    /**
     * Trouve toutes les parcelles avec un dispositif
     * @return Liste des parcelles avec un dispositif
     */
    @Query("SELECT p FROM Parcelle p WHERE p.dispositif IS NOT NULL")
    List<Parcelle> findAllWithDispositif();

    /**
     * Met à jour le taux d'humidité d'une parcelle
     * @param parcelleId ID de la parcelle
     * @param nouveauTaux Nouveau taux d'humidité
     */
    @Modifying
    @Query("UPDATE Parcelle p SET p.tauxHumidite = :taux WHERE p.id = :id")
    void updateTauxHumidite(@Param("id") Long parcelleId, @Param("taux") double nouveauTaux);

    /**
     * Compte le nombre de parcelles par taux d'humidité
     * @param minHumidite Taux d'humidité minimal
     * @param maxHumidite Taux d'humidité maximal
     * @return Nombre de parcelles dans la plage d'humidité
     */
    long countByTauxHumiditeBetween(double minHumidite, double maxHumidite);

    /**
     * Trouve les parcelles par simulation
     * @param simulationId ID de la simulation
     * @return Liste des parcelles de la simulation
     */
    @Query("SELECT p FROM Parcelle p WHERE p.simulation.id = :simulationId")
    List<Parcelle> findBySimulationId(@Param("simulationId") Long simulationId);

    /**
     * Trouve les parcelles voisines d'une parcelle donnée
     * @param posX Position X de la parcelle centrale
     * @param posY Position Y de la parcelle centrale
     * @return Liste des parcelles voisines
     */
    @Query("SELECT p FROM Parcelle p WHERE " +
            "((abs(p.posX - :x) <= 1 AND abs(p.posY - :y) <= 1) AND " +
            "(p.posX != :x OR p.posY != :y))")
    List<Parcelle> findVoisines(@Param("x") int posX, @Param("y") int posY);
}
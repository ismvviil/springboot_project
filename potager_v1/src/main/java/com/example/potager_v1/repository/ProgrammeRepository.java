package com.example.potager_v1.repository;

import com.example.potager_v1.model.traitement.Programme;
import com.example.potager_v1.model.traitement.TypeTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {
    List<Programme> findByProduit(TypeTraitement produit);

    @Query("SELECT p FROM Programme p WHERE p.dispositif.id = :dispositifId")
    List<Programme> findByDispositifId(@Param("dispositifId") Long dispositifId);

    // Modification de la requête pour éviter l'opérateur modulo
    @Query("SELECT p FROM Programme p WHERE :pas >= p.debut AND MOD((:pas - p.debut), p.periode) < p.duree")
    List<Programme> findActiveAtStep(@Param("pas") int pasSimulation);

    List<Programme> findByPeriode(int periode);

    List<Programme> findByDebut(int debut);

    long countByProduit(TypeTraitement produit);
}
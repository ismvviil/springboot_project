package com.example.potager_v1.repository;

import com.example.potager_v1.model.traitement.Dispositif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispositifRepository extends JpaRepository<Dispositif, Long> {

    /**
     * Trouve tous les dispositifs par rayon d'action
     * @param rayon Rayon d'action
     * @return Liste des dispositifs avec ce rayon
     */
    List<Dispositif> findByRayon(int rayon);

    /**
     * Trouve tous les dispositifs d'une parcelle
     * @param parcelleId ID de la parcelle
     * @return Liste des dispositifs de la parcelle (normalement un seul)
     */
    @Query("SELECT d FROM Dispositif d WHERE d.parcelle.id = :parcelleId")
    List<Dispositif> findByParcelleId(@Param("parcelleId") Long parcelleId);

    /**
     * Trouve tous les dispositifs ayant des programmes d'arrosage
     * @return Liste des dispositifs avec programmes d'arrosage
     */
    @Query("SELECT DISTINCT d FROM Dispositif d JOIN d.programmes p WHERE p.produit = com.example.potager_v1.model.traitement.TypeTraitement.EAU")
    List<Dispositif> findAllWithArrosage();

    /**
     * Trouve tous les dispositifs ayant des programmes d'insecticide
     * @return Liste des dispositifs avec programmes d'insecticide
     */
    @Query("SELECT DISTINCT d FROM Dispositif d JOIN d.programmes p WHERE p.produit = com.example.potager_v1.model.traitement.TypeTraitement.INSECTICIDE")
    List<Dispositif> findAllWithInsecticide();

    /**
     * Trouve tous les dispositifs ayant des programmes d'engrais
     * @return Liste des dispositifs avec programmes d'engrais
     */
    @Query("SELECT DISTINCT d FROM Dispositif d JOIN d.programmes p WHERE p.produit = com.example.potager_v1.model.traitement.TypeTraitement.ENGRAIS")
    List<Dispositif> findAllWithEngrais();

    /**
     * Compte le nombre de dispositifs par type de programme
     * @param typeProduit Type de produit
     * @return Nombre de dispositifs avec ce type de programme
     */
    @Query("SELECT COUNT(DISTINCT d) FROM Dispositif d JOIN d.programmes p WHERE p.produit = :type")
    long countByProgrammeType(@Param("type") com.example.potager_v1.model.traitement.TypeTraitement typeProduit);
}
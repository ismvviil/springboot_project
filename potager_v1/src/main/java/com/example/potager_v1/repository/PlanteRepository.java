package com.example.potager_v1.repository;

import com.example.potager_v1.model.enums.EspecePlante;
import com.example.potager_v1.model.Plante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanteRepository extends JpaRepository<Plante, Long> {

    /**
     * Trouve toutes les plantes d'une espèce donnée
     * @param espece Espèce de plante
     * @return Liste des plantes de cette espèce
     */
    List<Plante> findByEspece(EspecePlante espece);

    /**
     * Trouve toutes les plantes matures
     * @return Liste des plantes matures
     */
    List<Plante> findByMatureTrue();

    /**
     * Trouve toutes les plantes pouvant produire des fruits
     * @return Liste des plantes pouvant produire des fruits
     */
    @Query("SELECT p FROM Plante p WHERE p.mature = true AND p.age >= p.ageMaturiteFruit AND p.nbRecoltesEffectuees < p.nbRecolteMax")
    List<Plante> findAllProductives();

    /**
     * Trouve toutes les plantes d'une parcelle
     * @param parcelleId ID de la parcelle
     * @return Liste des plantes de la parcelle
     */
    @Query("SELECT p FROM Plante p WHERE p.parcelle.id = :parcelleId")
    List<Plante> findByParcelleId(@Param("parcelleId") Long parcelleId);

    /**
     * Compte le nombre de plantes par espèce
     * @param espece Espèce de plante
     * @return Nombre de plantes de cette espèce
     */
    long countByEspece(EspecePlante espece);

    @Query("SELECT p FROM Plante p WHERE TYPE(p) = com.example.potager_v1.model.PlanteDrageonnante")
    List<Plante> findAllDrageonnantes();


    /**
     * Trouve les plantes dont l'humidité de la parcelle est adaptée
     * @return Liste des plantes dans des conditions d'humidité adaptées
     */
    @Query("SELECT p FROM Plante p WHERE p.parcelle.tauxHumidite BETWEEN p.humiditeMin AND p.humiditeMax")
    List<Plante> findAllWithHumiditeAdaptee();

    /**
     * Trouve les plantes dont l'humidité de la parcelle n'est pas adaptée
     * @return Liste des plantes dans des conditions d'humidité non adaptées
     */
    @Query("SELECT p FROM Plante p WHERE p.parcelle.tauxHumidite < p.humiditeMin OR p.parcelle.tauxHumidite > p.humiditeMax")
    List<Plante> findAllWithHumiditeNonAdaptee();
}
package com.example.potager_v1.repository;

import com.example.potager_v1.model.enums.EspeceInsecte;
import com.example.potager_v1.model.Insecte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsecteRepository extends JpaRepository<Insecte, Long> {
    List<Insecte> findByEspece(EspeceInsecte espece);
    List<Insecte> findBySexe(String sexe);

    @Query("SELECT i FROM Insecte i WHERE i.espece IN :nuisibleEspeces")
    List<Insecte> findAllNuisibles(@Param("nuisibleEspeces") List<EspeceInsecte> nuisibleEspeces);

    @Query("SELECT i FROM Insecte i WHERE i.espece IN :benefiqueEspeces")
    List<Insecte> findAllBenefiques(@Param("benefiqueEspeces") List<EspeceInsecte> benefiqueEspeces);

    @Query("SELECT i FROM Insecte i WHERE i.parcelle.id = :parcelleId")
    List<Insecte> findByParcelleId(@Param("parcelleId") Long parcelleId);

    @Query("SELECT i FROM Insecte i WHERE i.sexe = 'F' AND i.tempsDepuisDerniereRepro >= i.tempsEntreRepro")
    List<Insecte> findAllReadyForReproduction();

    long countByEspece(EspeceInsecte espece);

    List<Insecte> findByResistanceInsecticideGreaterThan(double seuilResistance);

    @Query("SELECT i FROM Insecte i WHERE (i.age * 1.0 / i.dureeVieMax) >= :seuil")
    List<Insecte> findAllOld(@Param("seuil") double seuilAge);
}
package com.example.potager_v1.repository;

import com.example.potager_v1.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    /**
     * Trouve une simulation par son nom
     * @param nom Nom de la simulation
     * @return La simulation correspondante (optionnelle)
     */
    Optional<Simulation> findByNom(String nom);

    /**
     * Trouve toutes les simulations actives
     * @return Liste des simulations actives
     */
    List<Simulation> findByEnCoursTrue();

    /**
     * Trouve toutes les simulations terminées
     * @return Liste des simulations terminées
     */
    List<Simulation> findByEnCoursFalse();

    /**
     * Trouve les simulations avec un nombre d'itérations supérieur à un seuil
     * @param seuil Seuil d'itérations
     * @return Liste des simulations dépassant ce seuil
     */
    List<Simulation> findByPasSimulationActuelGreaterThan(int seuil);

    /**
     * Trouve la simulation la plus récente
     * @return La simulation la plus récente
     */
    @Query("SELECT s FROM Simulation s ORDER BY s.dateCreation DESC")
    List<Simulation> findMostRecent();

    /**
     * Trouve les simulations créées entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des simulations créées dans cet intervalle
     */
    List<Simulation> findByDateCreationBetween(Date dateDebut, Date dateFin);

    /**
     * Compte le nombre de simulations par état (en cours ou terminées)
     * @param enCours État de la simulation
     * @return Nombre de simulations dans cet état
     */
    long countByEnCours(boolean enCours);

    /**
     * Trouve les simulations avec le plus grand nombre d'itérations
     * @param limit Nombre maximum de simulations à retourner
     * @return Liste des simulations avec le plus grand nombre d'itérations
     */
    @Query("SELECT s FROM Simulation s ORDER BY s.pasSimulationActuel DESC")
    List<Simulation> findWithMostIterations(int limit);
}
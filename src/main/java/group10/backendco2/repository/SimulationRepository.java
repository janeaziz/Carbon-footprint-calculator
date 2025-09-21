package group10.backendco2.repository;

import group10.backendco2.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de repository pour l'entité Simulation.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    /**
     * Trouve toutes les simulations associées à un utilisateur donné.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return une liste d'objets Simulation
     */
    List<Simulation> findByUtilisateurId(Long utilisateurId);
}

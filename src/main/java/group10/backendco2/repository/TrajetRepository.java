package group10.backendco2.repository;

import group10.backendco2.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Interface de repository pour l'entité Trajet.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    /**
     * Trouve un trajet par son identifiant et charge les modes de transport associés.
     *
     * @param id l'identifiant du trajet
     * @return une instance de Trajet avec les modes de transport chargés
     */
    @Query("SELECT t FROM Trajet t LEFT JOIN FETCH t.modesTransport WHERE t.id = :id")
    Optional<Trajet> findByIdWithModes(@Param("id") Long id);
}

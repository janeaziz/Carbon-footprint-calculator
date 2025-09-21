package group10.backendco2.repository;

import group10.backendco2.model.SourceEnergie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour l'entité SourceEnergie.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
@Repository
public interface SourceEnergieRepository extends JpaRepository<SourceEnergie, Long> {
}

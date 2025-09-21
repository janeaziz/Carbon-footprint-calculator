package group10.backendco2.repository;

import group10.backendco2.model.SourceElectrique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Interface de repository pour l'entité SourceElectrique.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
public interface SourceElectriqueRepository extends JpaRepository<SourceElectrique, Long> {

    /**
     * Trouve une source électrique par son identifiant de source d'énergie.
     *
     * @param sourceEnergieId l'identifiant de la source d'énergie
     * @return une instance de SourceElectrique si trouvée, sinon Optional vide
     */
    Optional<SourceElectrique> findBySourceEnergieId(Long sourceEnergieId);
}

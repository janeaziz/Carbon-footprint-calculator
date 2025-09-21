package group10.backendco2.repository;

import group10.backendco2.model.CarburantFossile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de gestion des données de carburants fossiles.
 * Permet d'effectuer des opérations CRUD sur les entités CarburantFossile.
 */
@Repository
public interface CarburantFossileRepository
    extends JpaRepository<CarburantFossile, String> {

  /**
   * Récupère tous les carburants fossiles associés à une source d'énergie
   * donnée.
   *
   * @param sourceEnergieId l'identifiant de la source d'énergie
   * @return une liste de carburants fossiles associés à la source d'énergie
   */
  List<CarburantFossile> findAllBySourceEnergieId(Long sourceEnergieId);
}

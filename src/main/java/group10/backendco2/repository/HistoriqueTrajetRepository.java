package group10.backendco2.repository;

import group10.backendco2.model.HistoriqueTrajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Interface de repository pour l'entité HistoriqueTrajet.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
public interface HistoriqueTrajetRepository extends JpaRepository<HistoriqueTrajet, Long> {

    /**
     * Trouve tous les trajets associés à un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste d'objets HistoriqueTrajet
     */
    List<HistoriqueTrajet> findByUtilisateurId(Long userId);
}

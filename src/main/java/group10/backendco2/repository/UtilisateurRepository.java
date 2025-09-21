package group10.backendco2.repository;

import group10.backendco2.model.Utilisateur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour l'entité Utilisateur.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
@Repository
public interface UtilisateurRepository
    extends JpaRepository<Utilisateur, Long> {
  @Query("SELECT u FROM Utilisateur u WHERE upper(u.email) = upper(:email)")

  /**
   * Trouve un utilisateur par son adresse e-mail.
   *
   * @param email l'adresse e-mail de l'utilisateur
   * @return un objet Utilisateur si trouvé, sinon Optional.empty()
   */
  Optional<Utilisateur> findByEmail(@Param("email") String email);

  /**
   * Vérifie si un utilisateur existe par son adresse e-mail.
   * @param email l'adresse e-mail de l'utilisateur
   * @return true si l'utilisateur existe, sinon false
   */
  boolean existsByEmail(String email);
}
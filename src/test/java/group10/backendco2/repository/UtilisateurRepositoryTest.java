package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.Utilisateur;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
/**
 * Tests d'intégration pour le repository {@link UtilisateurRepository}.
 *
 * Vérifie la méthode personnalisée {@code findByEmail(String email)} pour
 * différents cas :
 * <ul>
 *   <li>Recherche d'un utilisateur par email (insensible à la casse)</li>
 *   <li>Vérification de l'existence d'un utilisateur par email</li>
 * </ul>
 */
@DataJpaTest
public class UtilisateurRepositoryTest {
  /**
   * Repository pour les utilisateurs.
   */
  @Autowired private UtilisateurRepository utilisateurRepository;
  /**
   * Vérifie que {@code findByEmail(String email)} retourne un utilisateur
   * correspondant à l'email donné, sans tenir compte de la casse.
   */
  @Test
  public void testFindByEmailCaseInsensitive() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setEmail("test@example.com");
    utilisateurRepository.save(utilisateur);

    Optional<Utilisateur> found =
        utilisateurRepository.findByEmail("TEST@example.com");

    assertThat(found).isPresent();
    assertThat(found.get().getEmail())
        .isEqualToIgnoringCase("test@example.com");
  }
  /**
   * Vérifie que {@code findByEmail(String email)} retourne un {@link Optional}
   * vide si aucun utilisateur n'est trouvé avec l'email donné.
   */
  @Test
  public void testExistsByEmail() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setEmail("exists@example.com");
    utilisateurRepository.save(utilisateur);

    boolean exists = utilisateurRepository.existsByEmail("exists@example.com");
    boolean notExists =
        utilisateurRepository.existsByEmail("nonexistent@example.com");

    assertThat(exists).isTrue();
    assertThat(notExists).isFalse();
  }
}
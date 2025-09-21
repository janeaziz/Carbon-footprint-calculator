
package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.HistoriqueTrajet;
import group10.backendco2.model.Utilisateur;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests d’intégration pour {@link HistoriqueTrajetRepository}.
 *
 * Vérifie que la méthode personnalisée {@code findByUtilisateurId(Long id)}
 * fonctionne correctement pour :
 * <ul>
 *   <li>un utilisateur avec plusieurs trajets historiques</li>
 *   <li>un utilisateur sans trajets</li>
 * </ul>
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HistoriqueTrajetRepositoryTest {

  /**
   * Repository pour les historiques de trajet.
   */
  @Autowired private HistoriqueTrajetRepository historiqueTrajetRepository;

  /**
   * Repository pour les utilisateurs.
   */
  @Autowired private UtilisateurRepository utilisateurRepository;
  /**
   * Vérifie que {@code findByUtilisateurId(id)} retourne tous les objets {@link
   * HistoriqueTrajet} associés à un utilisateur donné.
   */
  @Test
  @DisplayName(
      "findByUtilisateurId should return historique trajets for given user id")
  void
  testFindByUtilisateurId() {
    Utilisateur user = new Utilisateur();
    user.setNom("Test User");
    utilisateurRepository.save(user);

    HistoriqueTrajet trajet1 = new HistoriqueTrajet();
    trajet1.setUtilisateur(user);
    historiqueTrajetRepository.save(trajet1);

    HistoriqueTrajet trajet2 = new HistoriqueTrajet();
    trajet2.setUtilisateur(user);
    historiqueTrajetRepository.save(trajet2);

    List<HistoriqueTrajet> found =
        historiqueTrajetRepository.findByUtilisateurId(user.getId());

    assertThat(found).hasSize(2);
    assertThat(found).allMatch(
        t -> t.getUtilisateur().getId().equals(user.getId()));
  }
  /**
   * Vérifie que {@code findByUtilisateurId(id)} retourne une liste vide
   * si l’utilisateur n’a aucun trajet historique enregistré.
   */
  @Test
  @DisplayName(
      "findByUtilisateurId should return empty list if user has no trajets")
  void
  testFindByUtilisateurIdReturnsEmpty() {
    Utilisateur user = new Utilisateur();
    user.setNom("No Trajets User");
    utilisateurRepository.save(user);

    List<HistoriqueTrajet> found =
        historiqueTrajetRepository.findByUtilisateurId(user.getId());

    assertThat(found).isEmpty();
  }
}
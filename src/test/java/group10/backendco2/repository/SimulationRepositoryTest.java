package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.Simulation;
import group10.backendco2.model.Utilisateur;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Tests d’intégration pour {@link SimulationRepository}.
 *
 * Vérifie la méthode personnalisée {@code findByUtilisateurId(Long id)} pour
 * les cas : <ul> <li>Un utilisateur ayant des simulations enregistrées</li>
 *   <li>Un utilisateur inexistant ou sans simulations</li>
 * </ul>
 */
@DataJpaTest
class SimulationRepositoryTest {

  /**
   * Repository pour les simulations.
   */
  @Autowired private SimulationRepository simulationRepository;
  /**
   * Repository pour les utilisateurs.
   */
  @Autowired private UtilisateurRepository utilisateurRepository;
  /**
   * Utilisateur pour les tests.
   */
  private Utilisateur utilisateur;
  /**
   * Prépare les données de test en créant un utilisateur et deux simulations
   * associées.
   */
  @BeforeEach
  void setUp() {
    utilisateur = new Utilisateur();
    utilisateur.setNom("Test User");
    utilisateur = utilisateurRepository.save(utilisateur);

    Simulation sim1 = new Simulation();
    sim1.setUtilisateur(utilisateur);
    simulationRepository.save(sim1);

    Simulation sim2 = new Simulation();
    sim2.setUtilisateur(utilisateur);
    simulationRepository.save(sim2);
  }
  /**
   * Vérifie que {@code findByUtilisateurId(Long)} retourne toutes les
   * simulations associées à un utilisateur existant.
   */
  @Test
  @DisplayName(
      "findByUtilisateurId should return simulations for given utilisateur id")
  void
  testFindByUtilisateurId() {
    List<Simulation> simulations =
        simulationRepository.findByUtilisateurId(utilisateur.getId());
    assertThat(simulations).hasSize(2);
    assertThat(simulations)
        .allMatch(
            sim -> sim.getUtilisateur().getId().equals(utilisateur.getId()));
  }
  /**
   * Vérifie que {@code findByUtilisateurId(Long)} retourne une liste vide
   * si l’identifiant de l’utilisateur est inconnu.
   */
  @Test
  @DisplayName(
      "findByUtilisateurId should return empty list for unknown utilisateur id")
  void
  testFindByUtilisateurIdWithUnknownId() {
    List<Simulation> simulations =
        simulationRepository.findByUtilisateurId(-1L);
    assertThat(simulations).isEmpty();
  }
}

package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.CarburantFossile;
import group10.backendco2.model.SourceEnergie;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
/**
 * Tests d'intégration pour le repository {@link CarburantFossileRepository}.
 *
 * Vérifie la méthode personnalisée {@code findAllBySourceEnergieId(Long id)}
 * pour différents cas :
 * <ul>
 *   <li>Récupération de plusieurs carburants liés à une source d’énergie</li>
 *   <li>Aucune correspondance pour un identifiant inexistant</li>
 * </ul>
 */
@DataJpaTest
class CarburantFossileRepositoryTest {

  /**
   * Repository pour les carburants fossiles.
   */
  @Autowired private CarburantFossileRepository carburantFossileRepository;
  /**
   * Repository pour les sources d’énergie.
   */
  @Autowired private SourceEnergieRepository sourceEnergieRepository;
  /**
   * Source d’énergie 1 pour les tests.
   */
  private SourceEnergie sourceEnergie1;
  /**
   * Source d’énergie 2 pour les tests.
   */
  private SourceEnergie sourceEnergie2;

  /**
   * Prépare les données de test en créant deux sources d’énergie et trois
   * carburants fossiles associés.
   */
  @BeforeEach
  void setUp() {
    sourceEnergie1 = new SourceEnergie();
    sourceEnergie1.setNom("Source 1");
    sourceEnergie1 = sourceEnergieRepository.save(sourceEnergie1);

    sourceEnergie2 = new SourceEnergie();
    sourceEnergie2.setNom("Source 2");
    sourceEnergie2 = sourceEnergieRepository.save(sourceEnergie2);

    CarburantFossile cf1 = new CarburantFossile();
    cf1.setType("CF1");
    cf1.setSourceEnergie(sourceEnergie1);

    CarburantFossile cf2 = new CarburantFossile();
    cf2.setType("CF2");
    cf2.setSourceEnergie(sourceEnergie1);

    CarburantFossile cf3 = new CarburantFossile();
    cf3.setType("CF3");
    cf3.setSourceEnergie(sourceEnergie2);

    carburantFossileRepository.save(cf1);
    carburantFossileRepository.save(cf2);
    carburantFossileRepository.save(cf3);
  }

  /**
   * Vérifie que {@code findAllBySourceEnergieId(id)} retourne tous les
   * carburants fossiles associés à une source d’énergie donnée.
   */
  @Test
  @DisplayName(
      "findAllBySourceEnergieId returns carburants for given sourceEnergieId")
  void
  testFindAllBySourceEnergieId() {
    List<CarburantFossile> result1 =
        carburantFossileRepository.findAllBySourceEnergieId(
            sourceEnergie1.getId());
    assertThat(result1).hasSize(2);

    List<CarburantFossile> result2 =
        carburantFossileRepository.findAllBySourceEnergieId(
            sourceEnergie2.getId());
    assertThat(result2).hasSize(1);
  }
  /**
   * Vérifie que {@code findAllBySourceEnergieId(id)} retourne une liste vide
   * lorsque l’identifiant de la source d’énergie n’est associé à aucun
   * carburant.
   */
  @Test
  @DisplayName(
      "findAllBySourceEnergieId returns empty list for unknown sourceEnergieId")
  void
  testFindAllBySourceEnergieIdEmpty() {
    List<CarburantFossile> result =
        carburantFossileRepository.findAllBySourceEnergieId(999L);
    assertThat(result).isEmpty();
  }
}
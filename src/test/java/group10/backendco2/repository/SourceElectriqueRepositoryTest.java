package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.SourceElectrique;
import group10.backendco2.model.SourceEnergie;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
/**
 * Tests d’intégration pour {@link SourceElectriqueRepository}.
 *
 * Vérifie que la méthode personnalisée {@code findBySourceEnergieId(Long)} :
 * <ul>
 *   <li>retourne une source électrique si elle existe pour une source d’énergie
 * donnée</li> <li>retourne un {@link Optional} vide si aucune correspondance
 * n’est trouvée</li>
 * </ul>
 */
@DataJpaTest
class SourceElectriqueRepositoryTest {
  /**
   * Repository pour les sources électriques.
   */
  @Autowired private SourceElectriqueRepository sourceElectriqueRepository;
  /**
   * Repository pour les sources d'énergie.
   */
  @Autowired private SourceEnergieRepository sourceEnergieRepository;
  /**
   * Vérifie que {@code findBySourceEnergieId(Long)} retourne une {@link
   * SourceElectrique} si elle est associée à une {@link SourceEnergie}
   * enregistrée.
   */
  @Test
  @DisplayName("findBySourceEnergieId returns SourceElectrique when present")
  void testFindBySourceEnergieId_found() {
    SourceEnergie sourceEnergie = new SourceEnergie();
    sourceEnergie = sourceEnergieRepository.save(sourceEnergie);

    SourceElectrique source = new SourceElectrique();
    source.setSourceEnergie(sourceEnergie);
    source = sourceElectriqueRepository.save(source);

    Optional<SourceElectrique> found =
        sourceElectriqueRepository.findBySourceEnergieId(
            source.getSourceEnergieId());

    assertThat(found).isPresent();
    assertThat(found.get().getSourceEnergieId())
        .isEqualTo(source.getSourceEnergieId());
  }
  /**
   * Vérifie que {@code findBySourceEnergieId(Long)} retourne un {@link
   * Optional} vide si aucun enregistrement n’est associé à l’identifiant donné.
   */
  @Test
  @DisplayName("findBySourceEnergieId returns empty when not present")
  void testFindBySourceEnergieId_notFound() {
    Optional<SourceElectrique> found =
        sourceElectriqueRepository.findBySourceEnergieId(999L);

    assertThat(found).isNotPresent();
  }
}
package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.SourceEnergie;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Tests d’intégration pour le repository {@link SourceEnergieRepository}.
 *
 * Vérifie les opérations de base sur l'entité {@link SourceEnergie} :
 * <ul>
 *   <li>Sauvegarde et récupération par identifiant</li>
 *   <li>Comportement lorsqu’un identifiant inexistant est recherché</li>
 *   <li>Suppression d’une entité</li>
 * </ul>
 */

@DataJpaTest
class SourceEnergieRepositoryTest {
  /**
   * Repository pour les sources d'énergie.
   */
  @Autowired private SourceEnergieRepository sourceEnergieRepository;
  /**
   * Vérifie que l'on peut enregistrer une {@link SourceEnergie} et la retrouver
   * ensuite en utilisant {@code findById(Long)}.
   */
  @Test
  @DisplayName("Should save and find SourceEnergie by id")
  void testSaveAndFindById() {
    SourceEnergie source = new SourceEnergie();
    source.setNom("Solaire");
    SourceEnergie saved = sourceEnergieRepository.save(source);

    Optional<SourceEnergie> found =
        sourceEnergieRepository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getNom()).isEqualTo("Solaire");
  }
  /**
   * Vérifie que {@code findById(Long)} retourne un {@link Optional} vide si
   * l’identifiant ne correspond à aucune entité {@link SourceEnergie}.
   */
  @Test
  @DisplayName("Should return empty when SourceEnergie not found")
  void testFindByIdNotFound() {
    Optional<SourceEnergie> found = sourceEnergieRepository.findById(999L);
    assertThat(found).isNotPresent();
  }
  /**
   * Vérifie que la suppression d'une {@link SourceEnergie} via {@code delete}
   * est effective, et que l'entité n'est plus retrouvable par son identifiant.
   */
  @Test
  @DisplayName("Should delete SourceEnergie")
  void testDelete() {
    SourceEnergie source = new SourceEnergie();
    source.setNom("Eolien");
    SourceEnergie saved = sourceEnergieRepository.save(source);

    sourceEnergieRepository.delete(saved);
    Optional<SourceEnergie> found =
        sourceEnergieRepository.findById(saved.getId());
    assertThat(found).isNotPresent();
  }
}
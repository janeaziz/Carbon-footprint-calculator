package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.TypeTransport;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Tests d'intégration pour le repository {@link TypeTransportRepository}.
 *
 * Vérifie les opérations de base : sauvegarde, recherche par ID, suppression et
 * récupération de tous les enregistrements.
 */
@DataJpaTest
public class TypeTransportRepositoryTest {
  /**
   * Repository pour les types de transport.
   */
  @Autowired private TypeTransportRepository repository;

  /**
   * Vérifie que l'on peut enregistrer un {@link TypeTransport} et le retrouver
   * ensuite en utilisant {@code findById(Long)}.
   */
  @Test
  public void testSaveAndFindById() {

    TypeTransport typeTransport = new TypeTransport();
    typeTransport.setNom("Bus");

    TypeTransport saved = repository.save(typeTransport);

    Optional<TypeTransport> retrieved = repository.findById(saved.getId());
    assertThat(retrieved).isPresent();
    assertThat(retrieved.get().getNom()).isEqualTo("Bus");
  }
  /**
   * Vérifie que la suppression d’un {@link TypeTransport} via {@code delete}
   * fonctionne correctement et qu’il n’est plus retrouvé par son identifiant.
   */
  @Test
  public void testDelete() {
    TypeTransport typeTransport = new TypeTransport();
    typeTransport.setNom("Train");
    TypeTransport saved = repository.save(typeTransport);

    repository.delete(saved);
    Optional<TypeTransport> retrieved = repository.findById(saved.getId());
    assertThat(retrieved).isNotPresent();
  }
  /**
   * Vérifie que la méthode {@code findAll()} retourne tous les types de
   * transport enregistrés.
   */
  @Test
  public void testFindAll() {
    TypeTransport typeTransport1 = new TypeTransport();
    typeTransport1.setNom("Airplane");
    TypeTransport typeTransport2 = new TypeTransport();
    typeTransport2.setNom("Ship");

    repository.save(typeTransport1);
    repository.save(typeTransport2);

    assertThat(repository.findAll()).hasSize(2);
  }
}
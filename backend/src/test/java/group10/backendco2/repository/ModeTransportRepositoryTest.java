
package group10.backendco2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import group10.backendco2.model.ModeTransport;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Tests d'intégration pour {@link ModeTransportRepository}.
 *
 * Vérifie le bon fonctionnement de la méthode personnalisée {@code
 * findByNomApprox(String)}, notamment : <ul> <li>la recherche insensible à la
 * casse</li> <li>la correspondance partielle</li> <li>le comportement
 * lorsqu'aucun résultat n'est trouvé</li>
 * </ul>
 */
@DataJpaTest
class ModeTransportRepositoryTest {
  /**
   * Repository pour les modes de transport.
   */
  @Autowired private ModeTransportRepository modeTransportRepository;
  /**
   * Prépare les données de test en créant plusieurs objets {@link
   * ModeTransport} avec des noms variés.
   */
  @BeforeEach
  void setUp() {
    modeTransportRepository.deleteAll();
    ModeTransport voiture = new ModeTransport();
    voiture.setNom("Voiture");
    modeTransportRepository.save(voiture);

    ModeTransport velo = new ModeTransport();
    velo.setNom("Vélo");
    modeTransportRepository.save(velo);

    ModeTransport bus = new ModeTransport();
    bus.setNom("Bus");
    modeTransportRepository.save(bus);

    ModeTransport moto = new ModeTransport();
    moto.setNom("Moto");
    modeTransportRepository.save(moto);
  }
  /**
   * Vérifie que la méthode {@code findByNomApprox(String)} retourne
   * correctement les objets {@link ModeTransport} en cas de correspondance
   * partielle, sans tenir compte de la casse (insensible à la casse).
   */
  @Test
  @DisplayName("findByNomApprox should return matching ModeTransport " +
               "(case-insensitive, partial match)")
  void
  testFindByNomApprox_PartialAndCaseInsensitive() {
    List<ModeTransport> result = modeTransportRepository.findByNomApprox("voi");
    assertThat(result)
        .extracting(ModeTransport::getNom)
        .anyMatch(nom -> nom.equalsIgnoreCase("Voiture"));

    result = modeTransportRepository.findByNomApprox("vé");
    assertThat(result)
        .extracting(ModeTransport::getNom)
        .anyMatch(nom -> nom.equalsIgnoreCase("Vélo"));

    result = modeTransportRepository.findByNomApprox("BUS");
    assertThat(result)
        .extracting(ModeTransport::getNom)
        .anyMatch(nom -> nom.equalsIgnoreCase("Bus"));
  }
  /**
   * Vérifie que la méthode {@code findByNomApprox(String)} retourne une liste
   * vide lorsqu'aucun nom ne correspond au critère de recherche.
   */
  @Test
  @DisplayName("findByNomApprox should return empty list if no match")
  void testFindByNomApprox_NoMatch() {
    List<ModeTransport> result =
        modeTransportRepository.findByNomApprox("avion");
    assertThat(result).isEmpty();
  }
}
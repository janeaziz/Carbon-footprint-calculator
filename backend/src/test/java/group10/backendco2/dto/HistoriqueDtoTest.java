
package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link HistoriqueDto}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link HistoriqueDto} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class HistoriqueDtoTest {

  /**
   * Teste le constructeur sans arguments de {@link HistoriqueDto}.
   * <p>
   * Cette méthode teste que les valeurs par défaut des attributs sont nulles.
   */
  @Test
  void testNoArgsConstructor() {
    HistoriqueDto dto = new HistoriqueDto();
    assertNull(dto.getId());
    assertNull(dto.getOrigine());
    assertNull(dto.getDestination());
    assertNull(dto.getDate());
    assertNull(dto.getModes());
    assertNull(dto.getContrainte());
  }
  /**
   * Vérifie que le constructeur complet de {@link HistoriqueDto} initialise
   * correctement tous les champs, et que les getters renvoient les bonnes
   * valeurs.
   */
  @Test
  void testAllArgsConstructorAndGetters() {
    Long id = 1L;
    String origine = "Lyon";
    String destination = "Paris";
    Date date = new Date();
    TransportEmissionDto mode = new TransportEmissionDto();
    List<TransportEmissionDto> modes = Arrays.asList(mode);
    String contrainte = "Aucune";

    HistoriqueDto dto =
        new HistoriqueDto(id, origine, destination, date, modes, contrainte);

    assertEquals(id, dto.getId());
    assertEquals(origine, dto.getOrigine());
    assertEquals(destination, dto.getDestination());
    assertEquals(date, dto.getDate());
    assertEquals(modes, dto.getModes());
    assertEquals(contrainte, dto.getContrainte());
  }
  /**
   * Teste les setters de {@link HistoriqueDto}.
   * <p>
   * Cette méthode teste que les valeurs des attributs sont correctement
   * définies.
   */
  @Test
  void testSetters() {
    HistoriqueDto dto = new HistoriqueDto();
    Long id = 2L;
    String origine = "Marseille";
    String destination = "Nice";
    Date date = new Date();
    TransportEmissionDto mode = new TransportEmissionDto();
    List<TransportEmissionDto> modes = Arrays.asList(mode);
    String contrainte = "Rapide";

    dto.setId(id);
    dto.setOrigine(origine);
    dto.setDestination(destination);
    dto.setDate(date);
    dto.setModes(modes);
    dto.setContrainte(contrainte);

    assertEquals(id, dto.getId());
    assertEquals(origine, dto.getOrigine());
    assertEquals(destination, dto.getDestination());
    assertEquals(date, dto.getDate());
    assertEquals(modes, dto.getModes());
    assertEquals(contrainte, dto.getContrainte());
  }
  /**
   * Teste la méthode {@link HistoriqueDto#toString()}.
   * <p>
   * Cette méthode teste que la chaîne de caractères renvoyée n'est pas nulle.
   */
  @Test
  void testEqualsAndHashCode() {
    Long id = 3L;
    String origine = "A";
    String destination = "B";
    Date date = new Date();
    List<TransportEmissionDto> modes =
        Arrays.asList(new TransportEmissionDto());
    String contrainte = "Test";

    HistoriqueDto dto1 =
        new HistoriqueDto(id, origine, destination, date, modes, contrainte);
    HistoriqueDto dto2 =
        new HistoriqueDto(id, origine, destination, date, modes, contrainte);

    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());
  }
  /**
   * Teste la méthode {@link HistoriqueDto#toString()}.
   * <p>
   * Cette méthode teste que la chaîne de caractères renvoyée n'est pas nulle.
   */
  @Test
  void testToString() {
    HistoriqueDto dto = new HistoriqueDto();
    assertNotNull(dto.toString());
  }
}
package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link SourceElectrique}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link SourceElectrique} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class SourceElectriqueTest {
  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructor() {
    SourceElectrique se = new SourceElectrique();
    assertNull(se.getSourceEnergieId());
    assertEquals(0.0f, se.getPrixKWH());
    assertNull(se.getSourceEnergie());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code sourceEnergieId}, {@code prixKWH} et
   * {@code sourceEnergie}.
   */
  @Test
  void testAllArgsConstructor() {
    SourceEnergie sourceEnergie = new SourceEnergie();
    Long id = 1L;
    float prix = 0.15f;
    SourceElectrique se = new SourceElectrique(id, prix, sourceEnergie);

    assertEquals(id, se.getSourceEnergieId());
    assertEquals(prix, se.getPrixKWH());
    assertEquals(sourceEnergie, se.getSourceEnergie());
  }
  /**
   * Vérifie que le champ {@code sourceEnergieId} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testSetAndGetSourceEnergieId() {
    SourceElectrique se = new SourceElectrique();
    se.setSourceEnergieId(2L);
    assertEquals(2L, se.getSourceEnergieId());
  }
  /**
   * Vérifie que le champ {@code prixKWH} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testSetAndGetPrixKWH() {
    SourceElectrique se = new SourceElectrique();
    se.setPrixKWH(0.25f);
    assertEquals(0.25f, se.getPrixKWH());
  }
  /**
   * Vérifie que le champ {@code sourceEnergie} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testSetAndGetSourceEnergie() {
    SourceElectrique se = new SourceElectrique();
    SourceEnergie sourceEnergie = new SourceEnergie();
    se.setSourceEnergie(sourceEnergie);
    assertEquals(sourceEnergie, se.getSourceEnergie());
  }
  /**
   * Vérifie que deux instances de {@link SourceElectrique} avec les mêmes
   * valeurs sont considérées comme égales et ont le même hashCode.
   */
  @Test
  void testEqualsAndHashCode() {
    SourceEnergie sourceEnergie = new SourceEnergie();
    SourceElectrique se1 = new SourceElectrique(1L, 0.2f, sourceEnergie);
    SourceElectrique se2 = new SourceElectrique(1L, 0.2f, sourceEnergie);

    assertEquals(se1, se2);
    assertEquals(se1.hashCode(), se2.hashCode());
  }
  /**
   * Vérifie que la méthode {@code toString()} de {@link SourceElectrique}
   * retourne une chaîne contenant les champs attendus.
   */
  @Test
  void testToString() {
    SourceEnergie sourceEnergie = new SourceEnergie();
    SourceElectrique se = new SourceElectrique(1L, 0.3f, sourceEnergie);
    String str = se.toString();
    assertTrue(str.contains("sourceEnergieId=1"));
    assertTrue(str.contains("prixKWH=0.3"));
  }
}
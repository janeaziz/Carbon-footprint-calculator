package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link CarburantFossile}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link CarburantFossile} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class SourceEnergieTest {
  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructorAndSettersAndGetters() {
    SourceEnergie se = new SourceEnergie();
    se.setId(10L);
    se.setNom("Diesel");
    se.setEmission(2390.0f);

    assertEquals(10L, se.getId());
    assertEquals("Diesel", se.getNom());
    assertEquals(2390.0f, se.getEmission());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code id}, {@code nom} et {@code emission}.
   */
  @Test
  void testAllArgsConstructor() {
    SourceEnergie se = new SourceEnergie(2L, "Electricité", 12.5f);

    assertEquals(2L, se.getId());
    assertEquals("Electricité", se.getNom());
    assertEquals(12.5f, se.getEmission());
  }
  /**
   * Vérifie que la méthode {@code setEmission(float)} affecte correctement
   * la valeur des émissions de CO₂ et que le getter retourne la bonne valeur.
   */
  @Test
  void testSetEmissionWithFloat() {
    SourceEnergie se = new SourceEnergie();
    se.setEmission(100.5f);
    assertEquals(100.5f, se.getEmission());
  }
  /**
   * Vérifie que la méthode {@code setNom(String)} définit correctement le nom
   * de la source d’énergie.
   */
  @Test
  void testSetNom() {
    SourceEnergie se = new SourceEnergie();
    se.setNom("Essence");
    assertEquals("Essence", se.getNom());
  }
  /**
   * Vérifie que la méthode {@code setId(Long)} définit correctement
   * l’identifiant de la source d’énergie.
   */
  @Test
  void testSetId() {
    SourceEnergie se = new SourceEnergie();
    se.setId(99L);
    assertEquals(99L, se.getId());
  }
}
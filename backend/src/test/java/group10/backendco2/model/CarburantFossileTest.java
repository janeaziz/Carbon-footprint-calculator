package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link CarburantFossile}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link CarburantFossile} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class CarburantFossileTest {
  /**
   * Vérifie que tous les getters et setters de {@link CarburantFossile}
   * fonctionnent correctement avec le constructeur sans argument.
   */
  @Test
  void testNoArgsConstructorAndSetters() {
    CarburantFossile carburant = new CarburantFossile();
    carburant.setType("Diesel");
    carburant.setPrix(1.75f);
    carburant.setDensite(0.84f);

    SourceEnergie source = new SourceEnergie();
    carburant.setSourceEnergie(source);

    assertEquals("Diesel", carburant.getType());
    assertEquals(1.75f, carburant.getPrix());
    assertEquals(0.84f, carburant.getDensite());
    assertEquals(source, carburant.getSourceEnergie());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * les champs, et que les champs non définis explicitement restent à {@code
   * null}.
   */
  @Test
  void testAllArgsConstructor() {
    SourceEnergie source = new SourceEnergie();
    CarburantFossile carburant =
        new CarburantFossile("Essence", 1.65f, 0.75f, source);

    assertEquals("Essence", carburant.getType());
    assertEquals(1.65f, carburant.getPrix());
    assertEquals(0.75f, carburant.getDensite());
    assertEquals(source, carburant.getSourceEnergie());
  }
}
package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link TypeTransport}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link TypeTransport} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class TypeTransportTest {

  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructor() {
    TypeTransport typeTransport = new TypeTransport();
    assertNull(typeTransport.getId());
    assertNull(typeTransport.getNom());
    assertNull(typeTransport.getDescription());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code id}, {@code nom} et {@code description}.
   */
  @Test
  void testAllArgsConstructor() {
    Long id = 1L;
    String nom = "Collectif";
    String description = "Includes buses, trams, and subways";
    TypeTransport typeTransport = new TypeTransport(id, nom, description);

    assertEquals(id, typeTransport.getId());
    assertEquals(nom, typeTransport.getNom());
    assertEquals(description, typeTransport.getDescription());
  }

  /**
   * Vérifie que les méthodes {@code setId}, {@code setNom} et {@code
   * setDescription} de la classe {@link TypeTransport} fonctionnent
   * correctement, et que les getters retournent les valeurs attendues.
   */
  @Test
  void testSettersAndGetters() {
    TypeTransport typeTransport = new TypeTransport();
    Long id = 2L;
    String nom = "Individuel";
    String description = "Includes cars, bikes, and scooters";

    typeTransport.setId(id);
    typeTransport.setNom(nom);
    typeTransport.setDescription(description);

    assertEquals(id, typeTransport.getId());
    assertEquals(nom, typeTransport.getNom());
    assertEquals(description, typeTransport.getDescription());
  }
}
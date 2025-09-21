package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link Trajet}.
 * <p>
 * Vérifie que les constructeurs (sans et avec arguments) et les accesseurs
 * (getters/setters) fonctionnent correctement.
 */
class TrajetTest {
  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructorAndSetters() {
    Trajet trajet = new Trajet();
    trajet.setId(1L);
    trajet.setOrigine("Lyon");
    trajet.setDestination("Paris");
    trajet.setDistance(465.5f);
    trajet.setContrainte("Estimated time: 5h 20m");
    trajet.setCo2(123.45f);

    assertEquals(1L, trajet.getId());
    assertEquals("Lyon", trajet.getOrigine());
    assertEquals("Paris", trajet.getDestination());
    assertEquals(465.5f, trajet.getDistance());
    assertEquals("Estimated time: 5h 20m", trajet.getContrainte());
    assertEquals(123.45f, trajet.getCo2());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code id}, {@code origine}, {@code destination},
   * {@code distance}, {@code contrainte}, {@code co2} et
   * {@code modesTransport}.
   */
  @Test
  void testAllArgsConstructor() {
    Set<ModeTransport> modes = new HashSet<>();
    ModeTransport mode = new ModeTransport();
    modes.add(mode);

    Trajet trajet =
        new Trajet(2L, "Marseille", "Nice", 200.0f, "No tolls", 50.0f, modes);

    assertEquals(2L, trajet.getId());
    assertEquals("Marseille", trajet.getOrigine());
    assertEquals("Nice", trajet.getDestination());
    assertEquals(200.0f, trajet.getDistance());
    assertEquals("No tolls", trajet.getContrainte());
    assertEquals(50.0f, trajet.getCo2());
    assertEquals(1, trajet.getModesTransport().size());
    assertTrue(trajet.getModesTransport().contains(mode));
  }
  /**
   * Vérifie que la liste {@code modesTransport} est initialisée par défaut
   * et qu’elle est vide à l’instanciation d’un objet {@link Trajet}.
   */
  @Test
  void testModesTransportDefaultInitialization() {
    Trajet trajet = new Trajet();
    assertNotNull(trajet.getModesTransport());
    assertTrue(trajet.getModesTransport().isEmpty());
  }
  /**
   * Vérifie que le champ {@code contrainte} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testGetContrainte() {
    Trajet trajet = new Trajet();
    trajet.setContrainte("Test constraint");
    assertEquals("Test constraint", trajet.getContrainte());
  }
}
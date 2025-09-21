package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link RouteModeResponse}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link RouteModeResponse} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class RouteModeResponseTest {
  /**
   * Vérifie que tous les getters et setters de {@link RouteModeResponse}
   * fonctionnent correctement avec le constructeur sans argument.
   */
  @Test
  void testNoArgConstructorAndSettersGetters() {
    RouteModeResponse response = new RouteModeResponse();

    response.setMode("Train");
    assertEquals("Train", response.getMode());

    response.setDistanceKm(425.5f);
    assertEquals(425.5f, response.getDistanceKm());

    response.setEstimatedTime("3h45");
    assertEquals("3h45", response.getEstimatedTime());

    Map<String, Float> transitModes = new HashMap<>();
    transitModes.put("Bus", 2.5f);
    transitModes.put("Métro", 3.0f);
    response.setTransitModes(transitModes);
    assertEquals(transitModes, response.getTransitModes());

    response.setTransitStepLabels("Bus → Métro → Train");
    assertEquals("Bus → Métro → Train", response.getTransitStepLabels());

    List<String> verboseSteps =
        Arrays.asList("Prendre le Bus ligne 34", "Changer à Gare Centrale",
                      "Prendre le Train TGV");
    response.setTransitStepLabelsVerbose(verboseSteps);
    assertEquals(verboseSteps, response.getTransitStepLabelsVerbose());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * les champs, et que les champs non définis explicitement restent à {@code
   * null}.
   */
  @Test
  void testAllArgsConstructor() {
    Map<String, Float> transitModes = new HashMap<>();
    transitModes.put("Bus", 2.5f);

    RouteModeResponse response =
        new RouteModeResponse("Train", 425.5f, "3h45", transitModes);

    assertEquals("Train", response.getMode());
    assertEquals(425.5f, response.getDistanceKm());
    assertEquals("3h45", response.getEstimatedTime());
    assertEquals(transitModes, response.getTransitModes());

    assertNull(response.getTransitStepLabels());
    assertNull(response.getTransitStepLabelsVerbose());
  }
  /**
   * Vérifie que les setters acceptent les valeurs {@code null} sans erreur
   * et que les getters renvoient bien {@code null} après affectation.
   */
  @Test
  void testSettersWithNullValues() {
    RouteModeResponse response = new RouteModeResponse();

    response.setMode(null);
    assertNull(response.getMode());

    response.setEstimatedTime(null);
    assertNull(response.getEstimatedTime());

    response.setTransitModes(null);
    assertNull(response.getTransitModes());

    response.setTransitStepLabels(null);
    assertNull(response.getTransitStepLabels());

    response.setTransitStepLabelsVerbose(null);
    assertNull(response.getTransitStepLabelsVerbose());
  }
}
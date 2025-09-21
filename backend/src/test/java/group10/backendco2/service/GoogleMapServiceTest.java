package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import group10.backendco2.dto.RouteModeResponse;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Tests unitaires pour la classe {@link GoogleMapService}.
 *
 * Ces tests utilisent Mockito pour simuler les appels à l’API Google Maps via
 * `curl`, et valider les comportements des méthodes suivantes : <ul> <li>{@code
 * fetchEstimatedTime} — durée estimée d’un trajet</li> <li>{@code
 * fetchDistanceKm} — distance estimée en kilomètres</li> <li>{@code
 * fetchAllTransportModes} — parsing complet des modes de transport</li>
 * </ul>
 */
class GoogleMapServiceTest {
  /**
   * Service pour interagir avec l'API Google Maps.
   */
  private GoogleMapService googleMapService;
  /**
   * Prépare le service GoogleMapService avant chaque test.
   *
   * Initialise le service et injecte une clé API fictive pour éviter les appels
   * réels.
   */
  @BeforeEach
  void setUp() {
    googleMapService = Mockito.spy(new GoogleMapService());
    ReflectionTestUtils.setField(googleMapService, "apiKey", "dummy-key");
  }
  /**
   * Vérifie que {@code fetchEstimatedTime} retourne un message correctement
   * formaté lorsque la réponse JSON contient une durée valide.
   */
  @Test
  void fetchEstimatedTime_returnsEstimatedTime_whenResponseIsValid()
      throws Exception {
    String mockResponse =
        new JSONObject()
            .put("routes",
                 new org.json.JSONArray().put(new JSONObject().put(
                     "legs", new org.json.JSONArray().put(new JSONObject().put(
                                 "duration",
                                 new JSONObject().put("text", "15 mins"))))))
            .toString();

    doReturn(mockResponse)
        .when(googleMapService)
        .executeCurlCommand(anyString());

    String result = googleMapService.fetchEstimatedTime("A", "B");
    assertEquals("Temps estimé: 15 mins", result);
  }
  /**
   * Vérifie que {@code fetchEstimatedTime} retourne "Temps estimé inconnu"
   * si la réponse est nulle.
   */
  @Test
  void fetchEstimatedTime_returnsUnknown_whenResponseIsNull() throws Exception {
    doReturn(null).when(googleMapService).executeCurlCommand(anyString());

    String result = googleMapService.fetchEstimatedTime("A", "B");
    assertEquals("Temps estimé inconnu", result);
  }
  /**
   * Vérifie que {@code fetchEstimatedTime} retourne "Temps estimé inconnu"
   * si le champ "routes" est vide.
   */
  @Test
  void fetchEstimatedTime_returnsUnknown_whenRoutesIsEmpty() throws Exception {
    String mockResponse =
        new JSONObject().put("routes", new org.json.JSONArray()).toString();
    doReturn(mockResponse)
        .when(googleMapService)
        .executeCurlCommand(anyString());

    String result = googleMapService.fetchEstimatedTime("A", "B");
    assertEquals("Temps estimé inconnu", result);
  }
  /**
   * Vérifie que {@code fetchEstimatedTime} retourne un message d'erreur
   * si une exception est levée durant l’appel.
   */
  @Test
  void fetchEstimatedTime_returnsError_whenExceptionThrown() throws Exception {
    doThrow(new RuntimeException("API error"))
        .when(googleMapService)
        .executeCurlCommand(anyString());

    String result = googleMapService.fetchEstimatedTime("A", "B");
    assertEquals("Erreur lors de la récupération du temps estimé", result);
  }
  /**
   * Vérifie que {@code fetchDistanceKm} retourne la distance en kilomètres
   * correctement parsée à partir d'une réponse valide.
   */
  @Test
  void fetchDistanceKm_shouldReturnDistance_whenValidResponse() {
    String response =
        new JSONObject()
            .put("routes",
                 new JSONArray().put(new JSONObject().put(
                     "legs",
                     new JSONArray().put(new JSONObject().put(
                         "distance", new JSONObject().put("value", 12345))))))
            .toString();

    doReturn(response).when(googleMapService).executeCurlCommand(anyString());

    float distance = googleMapService.fetchDistanceKm("Lyon", "Paris");
    assertEquals(12.345f, distance, 0.01);
  }
  /**
   * Vérifie que {@code fetchAllTransportModes} analyse correctement les
   * différents modes, y compris les étapes de transport en commun.
   */
  @Test
  void fetchAllTransportModes_shouldParseAllModes() {
    String transitStep =
        new JSONObject()
            .put("travel_mode", "TRANSIT")
            .put("distance", new JSONObject().put("value", 5000))
            .put("transit_details",
                 new JSONObject()
                     .put("departure_stop", new JSONObject().put("name", "A"))
                     .put("arrival_stop", new JSONObject().put("name", "B"))
                     .put("line", new JSONObject()
                                      .put("short_name", "T1")
                                      .put("vehicle", new JSONObject().put(
                                                          "type", "TRAM"))))
            .toString();

    String fullResponse =
        new JSONObject()
            .put("routes",
                 new JSONArray().put(new JSONObject().put(
                     "legs",
                     new JSONArray().put(
                         new JSONObject()
                             .put("distance",
                                  new JSONObject().put("value", 8000))
                             .put("duration",
                                  new JSONObject().put("text", "30 mins"))
                             .put("steps", new JSONArray().put(
                                               new JSONObject(transitStep)))))))
            .toString();

    doReturn(fullResponse)
        .when(googleMapService)
        .executeCurlCommand(contains("transit"));

    List<RouteModeResponse> result =
        googleMapService.fetchAllTransportModes("A", "B");
    assertFalse(result.isEmpty());
    assertEquals("transit", result.get(0).getMode());
    assertEquals(8.0f, result.get(0).getDistanceKm(), 0.01);
    assertEquals("30 mins", result.get(0).getEstimatedTime());
    assertTrue(result.get(0).getTransitModes().containsKey("TRAM"));
    assertEquals("TRAM", result.get(0).getTransitStepLabels());
  }
}
package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.RouteModeResponse;
import group10.backendco2.service.GoogleMapService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test unitaire pour la classe {@link RouteModeController}.
 * <p>
 * Cette classe teste la méthode {@link
 * RouteModeController#getRoutesByModes(String, String)} pour s'assurer qu'elle
 * fonctionne correctement.
 */
class RouteModeControllerTest {
  /**
   * Instance de {@link GoogleMapService} pour simuler le service Google Maps.
   */
  private GoogleMapService googleMapService;
  /**
   * Instance de {@link RouteModeController} à tester.
   */
  private RouteModeController controller;

  /**
   * Méthode exécutée avant chaque test pour initialiser les instances
   * nécessaires.
   */
  @BeforeEach
  void setup() {
    googleMapService = mock(GoogleMapService.class);
    controller = new RouteModeController(googleMapService);
  }
  /**
   * Teste la méthode {@link RouteModeController#getRoutesByModes(String,
   * String)}. <p> Cette méthode teste la récupération des itinéraires par modes
   * de transport et vérifie que la réponse est correcte.
   */
  @Test
  void testGetRoutesByModes() {
    RouteModeResponse response1 = mock(RouteModeResponse.class);
    RouteModeResponse response2 = mock(RouteModeResponse.class);

    when(googleMapService.fetchAllTransportModes("Lyon", "Paris"))
        .thenReturn(Arrays.asList(response1, response2));

    List<RouteModeResponse> result =
        controller.getRoutesByModes("Lyon", "Paris");

    assertEquals(2, result.size());
    verify(googleMapService, times(1)).fetchAllTransportModes("Lyon", "Paris");
  }
}


package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.RouteModeResponse;
import group10.backendco2.model.CarburantFossile;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.SourceElectrique;
import group10.backendco2.model.SourceEnergie;
import group10.backendco2.model.Trajet;
import group10.backendco2.repository.CarburantFossileRepository;
import group10.backendco2.repository.ModeTransportRepository;
import group10.backendco2.repository.SourceElectriqueRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
/**
 * Test unitaire pour la classe {@link TransportEmissionService}.
 *
 * Cette classe vérifie :
 * <ul>
 *   <li>le calcul des émissions de CO₂ selon différents scénarios de
 * trajets</li> <li>la correspondance des modes Google Maps et des modes
 * internes</li> <li>la gestion des tarifs et des unités selon la source
 * d'énergie</li> <li>les méthodes internes comme l'extraction de la durée ou le
 * nettoyage des étiquettes</li>
 * </ul>
 */
class TransportEmissionServiceTest {
  /**
   * Repository pour les modes de transport.
   */
  @Mock private ModeTransportRepository modeTransportRepository;
  /**
   * Repository pour les carburants fossiles.
   */
  @Mock private CarburantFossileRepository carburantFossileRepository;
  /**
   * Repository pour les sources électriques.
   */
  @Mock private SourceElectriqueRepository sourceElectriqueRepository;
  /**
   * Service pour les émissions de transport.
   */
  @Mock private GoogleMapService googleMapService;
  /**
   * Service pour les émissions de transport.
   */
  @InjectMocks private TransportEmissionService service;
  /**
   * Prépare le service TransportEmissionService avant chaque test.
   *
   * Initialise les mocks et injecte les dépendances.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }
  /**
   * Crée un mode de transport avec les paramètres spécifiés.
   *
   * @param nom le nom du mode de transport
   * @param emission l'émission de CO₂ par km
   * @param consommation la consommation moyenne
   * @param tarifPublic le tarif public par km
   * @param sourceId l'identifiant de la source d'énergie
   * @return un objet ModeTransport configuré
   */
  private ModeTransport createModeTransport(String nom, float emission,
                                            float consommation,
                                            Float tarifPublic, Long sourceId) {
    SourceEnergie sourceEnergie = new SourceEnergie();
    sourceEnergie.setId(sourceId);
    sourceEnergie.setEmission(emission);

    ModeTransport mode = new ModeTransport();
    mode.setNom(nom);
    mode.setSourceEnergie(sourceEnergie);
    mode.setConsommationMoyenne(consommation);
    mode.setTarifPublicParKm(tarifPublic);
    return mode;
  }
  /**
   * Vérifie que les émissions sont correctement calculées à partir
   * d’un trajet contenant des modes explicites.
   */
  @Test
  void testCalculateEmissions_WithModes() {
    ModeTransport voiture = createModeTransport("Voiture", 120f, 6f, null, 1L);
    Set<ModeTransport> modes = new HashSet<>();
    modes.add(voiture);

    Trajet trajet = mock(Trajet.class);
    when(trajet.getModesTransport()).thenReturn(modes);
    when(trajet.getDistance()).thenReturn(10f);

    Map<String, Float> result = service.calculateEmissions(trajet);

    assertEquals(1, result.size());
    assertEquals(1200f, result.get("Voiture"));
  }

  /**
   * Vérifie que les émissions sont calculées avec tous les modes disponibles
   * si le trajet ne fournit aucun mode explicite.
   */
  @Test
  void testCalculateEmissions_NoModes() {
    ModeTransport bus = createModeTransport("Bus", 80f, 20f, null, 2L);
    List<ModeTransport> allModes = List.of(bus);

    Trajet trajet = mock(Trajet.class);
    when(trajet.getModesTransport()).thenReturn(null);
    when(trajet.getDistance()).thenReturn(5f);
    when(modeTransportRepository.findAll()).thenReturn(allModes);

    Map<String, Float> result = service.calculateEmissions(trajet);

    assertEquals(1, result.size());
    assertEquals(400f, result.get("Bus"));
  }

  /**
   * Teste l’extraction des minutes à partir d’une chaîne textuelle représentant
   * une durée.
   */
  @Test
  void testExtractMinutesFromText() {
    try {
      var method = TransportEmissionService.class.getDeclaredMethod(
          "extractMinutesFromText", String.class);
      method.setAccessible(true);
      assertEquals(65f, (float)method.invoke(service, "1 hour 5 min"));
      assertEquals(1440f, (float)method.invoke(service, "1 day"));
      assertEquals(1505f, (float)method.invoke(service, "1 day 1 hour 5 min"));
      assertEquals(0f, (float)method.invoke(service, "invalid"));
    } catch (Exception e) {
      fail(e);
    }
  }
  /**
   * Vérifie que les labels Google Maps sont correctement transformés
   * en étiquettes normalisées internes.
   */
  @Test
  void testFormatLabel() throws Exception {
    var method = TransportEmissionService.class.getDeclaredMethod("formatLabel",
                                                                  String.class);
    method.setAccessible(true);
    assertEquals("Bus", method.invoke(service, "bus"));
    assertEquals("Metro", method.invoke(service, "subway"));
    assertEquals("Tram", method.invoke(service, "tram"));
    assertEquals("TER", method.invoke(service, "train"));
    assertEquals("TGV", method.invoke(service, "high_speed_train"));
    assertEquals("Ferry", method.invoke(service, "ferry"));
    assertEquals("custom", method.invoke(service, "custom"));
  }
  /**
   * Vérifie le calcul de la consommation et du prix si un tarif public par km
   * est fourni.
   */
  @Test
  void testCalculerConsoEtPrix_WithTarifPublic() {
    ModeTransport mode = createModeTransport("Bus", 0f, 10f, 0.5f, 1L);
    TransportEmissionService.EnergieEtPrix result =
        service.calculerConsoEtPrix(mode, 20f);
    assertEquals(2f, result.consommation(), 0.01f);
    assertEquals("kWh", result.unite());
    assertEquals(10f, result.prix(), 0.01f);
  }
  /**
   * Vérifie le calcul de la consommation et du prix lorsqu’un carburant fossile
   * est utilisé.
   */
  @Test
  void testCalculerConsoEtPrix_WithCarburantFossile() {
    ModeTransport mode = createModeTransport("Voiture", 0f, 8f, null, 1L);
    CarburantFossile fossile = new CarburantFossile();
    fossile.setPrix(2f);
    when(carburantFossileRepository.findAllBySourceEnergieId(1L))
        .thenReturn(List.of(fossile));

    TransportEmissionService.EnergieEtPrix result =
        service.calculerConsoEtPrix(mode, 50f);
    assertEquals(4f, result.consommation(), 0.01f);
    assertEquals("L", result.unite());
    assertEquals(8f, result.prix(), 0.01f);
  }
  /**
   * Vérifie le calcul de la consommation et du prix lorsqu’une source
   * électrique est utilisée.
   */
  @Test
  void testCalculerConsoEtPrix_WithSourceElectrique() {
    ModeTransport mode = createModeTransport("Tram", 0f, 5f, null, 2L);
    when(carburantFossileRepository.findAllBySourceEnergieId(2L))
        .thenReturn(Collections.emptyList());
    SourceElectrique elec = new SourceElectrique();
    elec.setPrixKWH(0.3f);
    when(sourceElectriqueRepository.findBySourceEnergieId(2L))
        .thenReturn(Optional.of(elec));

    TransportEmissionService.EnergieEtPrix result =
        service.calculerConsoEtPrix(mode, 10f);
    assertEquals(0.5f, result.consommation(), 0.01f);
    assertEquals("kWh", result.unite());
    assertEquals(0.15f, result.prix(), 0.01f);
  }
  /**
   * Teste la correspondance entre les noms des modes internes
   * et les noms renvoyés par Google Maps.
   */
  @Test
  void testMatchesGoogleMode() throws Exception {
    var method = TransportEmissionService.class.getDeclaredMethod(
        "matchesGoogleMode", String.class, String.class);
    method.setAccessible(true);
    assertTrue((Boolean)method.invoke(service, "Voiture", "driving"));
    assertTrue((Boolean)method.invoke(service, "Vélo", "bicycling"));
    assertTrue((Boolean)method.invoke(service, "Marche", "walking"));
    assertFalse((Boolean)method.invoke(service, "Bus", "driving"));
  }

  /**
   * Vérifie que les parenthèses dans les étiquettes de mode sont supprimées
   * proprement.
   */
  @Test
  void testRemoveParenthesesSafely() throws Exception {
    var method = TransportEmissionService.class.getDeclaredMethod(
        "removeParenthesesSafely", String.class);
    method.setAccessible(true);
    assertEquals("Bus", method.invoke(service, "Bus (Express)"));
    assertEquals("Train", method.invoke(service, "Train (TGV)"));
    assertEquals("Simple", method.invoke(service, "Simple"));
  }
  /**
   * Vérifie que les résultats complets de Google Maps (driving + transit) sont
   * traités correctement, et que les DTOs produits reflètent les sous-modes et
   * émissions correctement calculés.
   */
  @Test
  void calculateMultiModeEmissions_shouldReturnValidDtosForDrivingAndTransit() {
    RouteModeResponse driving = new RouteModeResponse();
    driving.setMode("driving");
    driving.setDistanceKm(100f);
    driving.setEstimatedTime("1 hour 30 min");

    RouteModeResponse transit = new RouteModeResponse();
    transit.setMode("transit");
    transit.setDistanceKm(120f);
    transit.setEstimatedTime("2 hours");
    Map<String, Float> subModes = new HashMap<>();
    subModes.put("BUS", 70f);
    subModes.put("TRAM", 50f);
    transit.setTransitModes(subModes);
    transit.setTransitStepLabelsVerbose(List.of("BUS Express", "TRAM T1"));

    when(googleMapService.fetchAllTransportModes(any(), any()))
        .thenReturn(List.of(driving, transit));

    ModeTransport voiture =
        createModeTransport("Voiture thermique", 234f, 6.5f, null, 1L);
    ModeTransport bus = createModeTransport("Bus", 100f, 12f, null, 2L);
    ModeTransport tram = createModeTransport("Tram", 80f, 9f, null, 3L);

    when(modeTransportRepository.findAll())
        .thenReturn(List.of(voiture, bus, tram));
    CarburantFossile diesel = new CarburantFossile();
    diesel.setType("Diesel");
    diesel.setPrix(1.75f);
    diesel.setDensite(0.84f);
    diesel.setSourceEnergie(new SourceEnergie(1L, "Diesel", 2390f));

    when(carburantFossileRepository.findAllBySourceEnergieId(anyLong()))
        .thenReturn(List.of(diesel));

    var result = service.calculateMultiModeEmissions("Lyon", "Grenoble");

    assertEquals(2, result.size());
    assertTrue(
        result.stream().anyMatch(r -> r.getMode().equals("Voiture thermique")));
    assertTrue(result.stream().anyMatch(
        r -> r.getMode().equals("Transport en commun")));
  }
}
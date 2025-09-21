package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Trajet;
import group10.backendco2.repository.TrajetRepository;
import group10.backendco2.service.GoogleMapService;
import group10.backendco2.service.ModeTransportService;
import group10.backendco2.service.TransportEmissionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test unitaire pour la classe {@link ModeTransportController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link ModeTransportController}
 * pour s'assurer qu'elles fonctionnent correctement.
 */
class ModeTransportControllerTest {

  /**
   * Instance de {@link ModeTransportController} à tester.
   */
  private ModeTransportController controller;
  /**
   * Instance de {@link TrajetRepository} pour simuler le référentiel des
   * trajets.
   */
  private TrajetRepository trajetRepository;
  /**
   * Instance de {@link GoogleMapService} pour simuler le service Google Maps.
   */
  private GoogleMapService googleMapService;
  /**
   * Instance de {@link ModeTransportService} pour simuler le service de
   * transport.
   */
  private ModeTransportService modeTransportService;
  /**
   * Instance de {@link TransportEmissionService} pour simuler le service
   * d'émissions.
   */
  private TransportEmissionService emissionService;

  /**
   * Méthode d'initialisation exécutée avant chaque test.
   * <p>
   * Cette méthode initialise les instances de {@link ModeTransportController},
   * {@link TrajetRepository}, {@link GoogleMapService}, {@link
   * ModeTransportService} et
   * {@link TransportEmissionService} pour les tests.
   */
  @BeforeEach
  void setUp() {
    controller = new ModeTransportController();
    trajetRepository = mock(TrajetRepository.class);
    googleMapService = mock(GoogleMapService.class);
    modeTransportService = mock(ModeTransportService.class);
    emissionService = mock(TransportEmissionService.class);

    ReflectionTestUtils.setField(controller, "trajetRepository",
                                 trajetRepository);
    ReflectionTestUtils.setField(controller, "googleMapService",
                                 googleMapService);
    ReflectionTestUtils.setField(controller, "service", modeTransportService);
    ReflectionTestUtils.setField(controller, "emissionService",
                                 emissionService);
  }

  /**
   * Teste la méthode {@link ModeTransportController#getAll()}.
   * <p>
   * Cette méthode teste la récupération de tous les modes de transport et
   * vérifie que la taille de la liste est correcte.
   */
  @Test
  void testGetAll() {
    ModeTransport mt = new ModeTransport();
    when(modeTransportService.getAll()).thenReturn(List.of(mt));

    List<ModeTransport> result = controller.getAll();
    assertEquals(1, result.size());
  }

  /**
   * Teste la méthode {@link ModeTransportController#getById(Long)}.
   * <p>
   * Cette méthode teste la récupération d'un mode de transport par son ID et
   * vérifie que le résultat est correct.
   */
  @Test
  void testCreate() {
    ModeTransport input = new ModeTransport();
    when(modeTransportService.save(input)).thenReturn(input);

    ModeTransport result = controller.create(input);
    assertEquals(input, result);
  }

  /**
   * Teste la méthode {@link ModeTransportController#getById(Long)}.
   * <p>
   * Cette méthode teste la récupération d'un mode de transport par son ID et
   * vérifie que le résultat est correct.
   */
  @Test
  void testUpdate() {
    ModeTransport updated = new ModeTransport();
    when(modeTransportService.update(eq(1L), any()))
        .thenReturn(java.util.Optional.of(updated));

    ModeTransport result = controller.update(1L, updated);
    assertEquals(updated, result);
  }

  /**
   * Teste la méthode {@link ModeTransportController#delete(Long)}.
   * <p>
   * Cette méthode teste la suppression d'un mode de transport par son ID et
   * vérifie que la méthode de service est appelée avec le bon ID.
   */
  @Test
  void testDelete() {
    controller.delete(1L);
    verify(modeTransportService).delete(1L);
  }

  /**
   * Teste la méthode {@link ModeTransportController#searchEmissions(String,
   * String)}. <p> Cette méthode teste la recherche d'émissions de transport
   * entre deux villes et vérifie que le résultat est correct.
   */
  @Test
  void testSearchEmissions() throws JsonProcessingException {
    TransportEmissionDto dto = new TransportEmissionDto();
    when(emissionService.calculateMultiModeEmissions("Lyon", "Paris"))
        .thenReturn(List.of(dto));

    List<TransportEmissionDto> result =
        controller.searchEmissions("Lyon", "Paris");
    assertEquals(1, result.size());
  }

  /**
   * Teste la méthode {@link
   * ModeTransportController#saveTrajetFromSearch(Trajet)}. <p> Cette méthode
   * teste l'enregistrement d'un trajet à partir de la recherche et vérifie que
   * le trajet est correctement enregistré.
   */
  @Test
  void testSaveTrajetFromSearch() {
    Trajet t = new Trajet();
    ResponseEntity<Void> response = controller.saveTrajetFromSearch(t);

    assertEquals(200, response.getStatusCode().value());
    verify(trajetRepository).save(t);
  }
}

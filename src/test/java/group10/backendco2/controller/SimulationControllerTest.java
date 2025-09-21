package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.SimulationRequest;
import group10.backendco2.model.Simulation;
import group10.backendco2.service.SimulationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link SimulationController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link SimulationController}
 * pour s'assurer qu'elles fonctionnent correctement.
 */
class SimulationControllerTest {

  /**
   * Instance de {@link SimulationService} pour simuler le service de
   * simulation.
   */
  private final SimulationService simulationService =
      mock(SimulationService.class);

  /**
   * Instance de {@link SimulationController} à tester.
   */
  private final SimulationController controller =
      new SimulationController(simulationService);

  /**
   * Teste la méthode {@link
   * SimulationController#enregistrerSimulation(SimulationRequest)}. <p> Cette
   * méthode teste l'enregistrement d'une simulation et vérifie que la réponse
   * est correcte.
   */
  @Test
  void testEnregistrerSimulation() {
    SimulationRequest request = new SimulationRequest();
    request.setUtilisateurId(1L);
    request.setOrigine("Lyon");
    request.setDestination("Paris");
    request.setModeTransport("Train");
    request.setFrequency("daily");
    request.setDuration(15);
    request.setTotalEmission(1234.5f);

    doNothing().when(simulationService).enregistrerSimulation(any());

    ResponseEntity<String> response = controller.enregistrerSimulation(request);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("✅ Simulation enregistrée avec succès.", response.getBody());
    verify(simulationService).enregistrerSimulation(any());
  }
  /**
   * Teste la méthode {@link SimulationController#getSimulationById(Long)}.
   * <p>
   * Cette méthode teste la récupération d'une simulation par son ID et
   * vérifie que la réponse est correcte.
   */
  @Test
  void testGetSimulationsByUser() {
    Simulation sim = new Simulation();
    sim.setId(1L);
    sim.setOrigine("Lyon");
    sim.setDestination("Paris");

    when(simulationService.getSimulationsByUser(1L)).thenReturn(List.of(sim));

    ResponseEntity<List<Simulation>> response =
        controller.getSimulationsByUser(1L);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Lyon", response.getBody().get(0).getOrigine());
  }
}

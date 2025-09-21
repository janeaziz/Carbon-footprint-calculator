package group10.backendco2.controller;

import group10.backendco2.dto.SimulationRequest;
import group10.backendco2.model.Simulation;
import group10.backendco2.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer les simulations d'empreinte carbone.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Enregistrer une nouvelle simulation</li>
 *   <li>Récupérer les simulations enregistrées d’un utilisateur</li>
 * </ul>
 */
@RestController
@RequestMapping("/simulations")
@RequiredArgsConstructor
public class SimulationController {
  /**
   * Service métier gérant les opérations liées aux simulations d'empreinte
   * carbone.
   */
  private final SimulationService simulationService;
  /**
   * Enregistre une nouvelle simulation d'empreinte carbone.
   *
   * @param request la requête contenant les détails de la simulation
   * @return une réponse indiquant le succès de l'opération
   */
  @PostMapping
  @Operation(
      summary = "Enregistrer une nouvelle simulation d'empreinte carbone")
  public ResponseEntity<String>
  enregistrerSimulation(@RequestBody SimulationRequest request) {
    simulationService.enregistrerSimulation(request);
    return ResponseEntity.ok("✅ Simulation enregistrée avec succès.");
  }
  /**
   * Récupère les simulations enregistrées d’un utilisateur.
   *
   * @param userId l'identifiant de l'utilisateur
   * @return une liste de simulations associées à l'utilisateur
   */
  @GetMapping("/{userId}")
  @Operation(
      summary = "Récupérer les simulations enregistrées d’un utilisateur")
  public ResponseEntity<List<Simulation>>
  getSimulationsByUser(@PathVariable Long userId) {
    List<Simulation> simulations =
        simulationService.getSimulationsByUser(userId);
    return ResponseEntity.ok(simulations);
  }
}

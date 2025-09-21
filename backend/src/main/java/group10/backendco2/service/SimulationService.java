package group10.backendco2.service;

import group10.backendco2.dto.SimulationRequest;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.Simulation;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.SimulationRepository;
import group10.backendco2.repository.UtilisateurRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Service responsable de la gestion des simulations d'émissions de CO2.
 * <ul>
 *   <li>Enregistrement d'une simulation</li>
 *   <li>Récupération des simulations par utilisateur</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class SimulationService {

  /**
   * Référentiel pour accéder aux données des simulations.
   */
  private final SimulationRepository simulationRepository;
  private final UtilisateurRepository utilisateurRepository;
  private final TransportEmissionService transportEmissionService;

  /**
   * Enregistre une simulation d'émissions de CO2.
   *
   * @param request l'objet {@link SimulationRequest} contenant les informations
   *     de la simulation
   */
  public void enregistrerSimulation(SimulationRequest request) {
    Utilisateur utilisateur =
        utilisateurRepository.findById(request.getUtilisateurId())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    List<TransportEmissionDto> allEmissions =
        transportEmissionService.calculateMultiModeEmissions(
            request.getOrigine(), request.getDestination());

    float emission =
        allEmissions.stream()
            .filter(
                e -> e.getMode().equalsIgnoreCase(request.getModeTransport()))
            .findFirst()
            .map(TransportEmissionDto::getCo2)
            .orElse(0f);

    float frequencyMultiplier = switch (request.getFrequency()) {
            case "weekly" -> 1f / 7;
            case "monthly" -> 1f / 30;
            default -> 1f;
        };
        float totalEmission = emission * request.getDuration() * frequencyMultiplier;

        Simulation simulation = new Simulation();
        simulation.setOrigine(request.getOrigine());
        simulation.setDestination(request.getDestination());
        simulation.setDateSimulation(new Date());
        simulation.setUtilisateur(utilisateur);
        simulation.setModeTransport(request.getModeTransport());
        simulation.setFrequency(request.getFrequency());
        simulation.setDuration(request.getDuration());
        simulation.setTotalEmission(totalEmission);

        simulationRepository.save(simulation);
    }

    /**
     * Récupère toutes les simulations d'un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste de toutes les simulations de l'utilisateur
     */
    public List<Simulation> getSimulationsByUser(Long userId) {
        return simulationRepository.findByUtilisateurId(userId);
    }
}

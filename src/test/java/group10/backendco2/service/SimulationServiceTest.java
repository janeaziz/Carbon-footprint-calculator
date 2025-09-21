package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.SimulationRequest;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Simulation;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.SimulationRepository;
import group10.backendco2.repository.UtilisateurRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test unitaire pour la classe {@link SimulationService}.
 *
 * Vérifie le bon enregistrement des simulations, la gestion des fréquences,
 * la sélection du mode de transport et le traitement des erreurs (utilisateur
 * introuvable).
 */
class SimulationServiceTest {

  /**
   * Repository pour les simulations.
   */
  private SimulationRepository simulationRepository;
  /**
   * Repository pour les utilisateurs.
   */
  private UtilisateurRepository utilisateurRepository;
  /**
   * Service pour les émissions de transport.
   */
  private TransportEmissionService transportEmissionService;
  /**
   * Service pour les simulations.
   */
  private SimulationService simulationService;
  /**
   * Prépare le service SimulationService avant chaque test.
   *
   * Initialise les repositories et injecte des instances fictives pour éviter
   * les appels réels.
   */
  @BeforeEach
  void setUp() {
    simulationRepository = mock(SimulationRepository.class);
    utilisateurRepository = mock(UtilisateurRepository.class);
    transportEmissionService = mock(TransportEmissionService.class);
    simulationService = new SimulationService(
        simulationRepository, utilisateurRepository, transportEmissionService);
  }
  /**
   * Vérifie que la simulation est enregistrée avec les valeurs correctes
   * lorsqu’un utilisateur et un mode de transport valide sont fournis.
   */
  @Test
  void enregistrerSimulation_shouldSaveSimulationWithCorrectValues() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setId(1L);

    SimulationRequest request = new SimulationRequest();
    request.setUtilisateurId(1L);
    request.setOrigine("A");
    request.setDestination("B");
    request.setModeTransport("bus");
    request.setFrequency("weekly");
    request.setDuration(14);

    TransportEmissionDto emissionDto = new TransportEmissionDto();
    emissionDto.setMode("bus");
    emissionDto.setCo2(10f);

    when(utilisateurRepository.findById(1L))
        .thenReturn(Optional.of(utilisateur));
    when(transportEmissionService.calculateMultiModeEmissions("A", "B"))
        .thenReturn(List.of(emissionDto));

    simulationService.enregistrerSimulation(request);

    ArgumentCaptor<Simulation> captor =
        ArgumentCaptor.forClass(Simulation.class);
    verify(simulationRepository).save(captor.capture());
    Simulation saved = captor.getValue();

    assertEquals("A", saved.getOrigine());
    assertEquals("B", saved.getDestination());
    assertEquals(utilisateur, saved.getUtilisateur());
    assertEquals("bus", saved.getModeTransport());
    assertEquals("weekly", saved.getFrequency());
    assertEquals(14, saved.getDuration());
    assertEquals(10f * 14 * (1f / 7), saved.getTotalEmission(), 0.0001);
    assertNotNull(saved.getDateSimulation());
  }
  /**
   * Vérifie que l’émission est fixée à zéro si le mode de transport demandé
   * n’est pas trouvé dans la liste des résultats calculés.
   */
  @Test
  void enregistrerSimulation_shouldUseZeroEmissionIfModeNotFound() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setId(2L);

    SimulationRequest request = new SimulationRequest();
    request.setUtilisateurId(2L);
    request.setOrigine("X");
    request.setDestination("Y");
    request.setModeTransport("train");
    request.setFrequency("monthly");
    request.setDuration(30);

    TransportEmissionDto emissionDto = new TransportEmissionDto();
    emissionDto.setMode("bus");
    emissionDto.setCo2(20f);

    when(utilisateurRepository.findById(2L))
        .thenReturn(Optional.of(utilisateur));
    when(transportEmissionService.calculateMultiModeEmissions("X", "Y"))
        .thenReturn(List.of(emissionDto));

    simulationService.enregistrerSimulation(request);

    ArgumentCaptor<Simulation> captor =
        ArgumentCaptor.forClass(Simulation.class);
    verify(simulationRepository).save(captor.capture());
    Simulation saved = captor.getValue();

    assertEquals(0f, saved.getTotalEmission(), 0.0001);
  }
  /**
   * Vérifie que l’enregistrement échoue avec une exception si l’utilisateur
   * n’est pas trouvé dans la base.
   */
  @Test
  void enregistrerSimulation_shouldThrowIfUserNotFound() {
    SimulationRequest request = new SimulationRequest();
    request.setUtilisateurId(99L);

    when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
      simulationService.enregistrerSimulation(request);
    });
    assertEquals("Utilisateur non trouvé", ex.getMessage());
    verify(simulationRepository, never()).save(any());
  }
  /**
   * Vérifie que la fréquence par défaut (non weekly/monthly) utilise un
   * multiplicateur 1.
   */
  @Test
  void enregistrerSimulation_shouldHandleDefaultFrequency() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setId(3L);

    SimulationRequest request = new SimulationRequest();
    request.setUtilisateurId(3L);
    request.setOrigine("C");
    request.setDestination("D");
    request.setModeTransport("car");
    request.setFrequency("daily");
    request.setDuration(5);

    TransportEmissionDto emissionDto = new TransportEmissionDto();
    emissionDto.setMode("car");
    emissionDto.setCo2(15f);

    when(utilisateurRepository.findById(3L))
        .thenReturn(Optional.of(utilisateur));
    when(transportEmissionService.calculateMultiModeEmissions("C", "D"))
        .thenReturn(List.of(emissionDto));

    simulationService.enregistrerSimulation(request);
    ArgumentCaptor<Simulation> captor =
        ArgumentCaptor.forClass(Simulation.class);
    verify(simulationRepository).save(captor.capture());
    Simulation saved = captor.getValue();

    assertEquals(15f * 5, saved.getTotalEmission(), 0.0001);
  }
}
package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.model.Trajet;
import group10.backendco2.repository.TrajetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link TrajetController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link TrajetController} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class TrajetControllerTest {
  /**
   * Instance de {@link TrajetRepository} pour simuler le référentiel des
   * trajets.
   */
  private TrajetRepository trajetRepository;
  /**
   * Instance de {@link TrajetController} à tester.
   */
  private TrajetController trajetController;

  /**
   * Méthode d'initialisation exécutée avant chaque test.
   * <p>
   * Cette méthode initialise les instances de {@link TrajetController} et
   * {@link TrajetRepository}.
   */
  @BeforeEach
  void setUp() {
    trajetRepository = mock(TrajetRepository.class);
    trajetController = new TrajetController(trajetRepository);
  }
  /**
   * Teste la méthode {@link TrajetController#createTrajet(Trajet)}.
   * <p>
   * Cette méthode teste la création d'un trajet et vérifie que le trajet est
   * correctement enregistré.
   */
  @Test
  void testCreateTrajet() {
    Trajet trajet = new Trajet();
    trajet.setOrigine("Lyon");
    trajet.setDestination("Paris");

    when(trajetRepository.save(any())).thenReturn(trajet);

    ResponseEntity<Trajet> response = trajetController.createTrajet(trajet);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Lyon", response.getBody().getOrigine());
    assertEquals("Paris", response.getBody().getDestination());

    verify(trajetRepository, times(1)).save(trajet);
  }
}

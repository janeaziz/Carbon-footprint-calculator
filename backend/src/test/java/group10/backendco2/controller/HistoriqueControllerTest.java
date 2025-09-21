package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.HistoriqueDto;
import group10.backendco2.model.HistoriqueTrajet;
import group10.backendco2.model.Trajet;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.HistoriqueTrajetRepository;
import group10.backendco2.repository.TrajetRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link HistoriqueController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link HistoriqueController}
 * pour s'assurer qu'elles fonctionnent correctement.
 */
class HistoriqueControllerTest {

  /**
   * Instance de {@link HistoriqueTrajetRepository} pour simuler le référentiel
   * des historiques de trajets.
   */
  private final HistoriqueTrajetRepository historiqueRepo =
      mock(HistoriqueTrajetRepository.class);
  /**
   * Instance de {@link TrajetRepository} pour simuler le référentiel des
   * trajets.
   */
  private final TrajetRepository trajetRepo = mock(TrajetRepository.class);
  /**
   * Instance de {@link HistoriqueController} à tester.
   */
  private final HistoriqueController controller =
      new HistoriqueController(historiqueRepo, trajetRepo);

  /**
   * Teste la méthode {@link HistoriqueController#addToHistory(Utilisateur,
   * Long)}. <p> Cette méthode teste l'ajout d'un trajet à l'historique d'un
   * utilisateur et vérifie que le trajet est correctement enregistré.
   */
  @Test
  void testAddToHistory() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);

    Trajet trajet = new Trajet();
    trajet.setId(42L);

    when(trajetRepo.findByIdWithModes(42L)).thenReturn(Optional.of(trajet));

    HistoriqueTrajet saved = new HistoriqueTrajet();
    saved.setId(99L);
    saved.setUtilisateur(user);
    saved.setTrajet(trajet);
    saved.setDateRealisation(new Date());

    when(historiqueRepo.save(any())).thenReturn(saved);

    HistoriqueTrajet result = controller.addToHistory(user, 42L);
    assertEquals(99L, result.getId());
    verify(historiqueRepo).save(any());
  }

  /**
   * Teste la méthode {@link HistoriqueController#getHistory(Utilisateur)}.
   * <p>
   * Cette méthode teste la récupération de l'historique des trajets d'un
   * utilisateur et vérifie que les trajets sont correctement renvoyés.
   */
  @Test
  void testGetHistory() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);

    Trajet trajet = new Trajet();
    trajet.setId(42L);
    trajet.setOrigine("Lyon");
    trajet.setDestination("Paris");

    HistoriqueTrajet historique = new HistoriqueTrajet();
    historique.setId(88L);
    historique.setUtilisateur(user);
    historique.setTrajet(trajet);
    historique.setDateRealisation(new Date());

    when(historiqueRepo.findByUtilisateurId(1L))
        .thenReturn(List.of(historique));
    when(trajetRepo.findByIdWithModes(42L)).thenReturn(Optional.of(trajet));

    List<HistoriqueDto> result = controller.getHistory(user);

    assertEquals(1, result.size());
    assertEquals("Lyon", result.get(0).getOrigine());
  }

  /**
   * Teste la méthode {@link HistoriqueController#deleteFromHistory(Utilisateur,
   * Long)}. <p> Cette méthode teste la suppression d'un trajet de l'historique
   * d'un utilisateur et vérifie que le trajet est correctement supprimé.
   */
  @Test
  void testDeleteFromHistory_Ok() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);

    HistoriqueTrajet historique = new HistoriqueTrajet();
    historique.setId(123L);
    historique.setUtilisateur(user);

    when(historiqueRepo.findById(123L)).thenReturn(Optional.of(historique));

    ResponseEntity<Void> response = controller.deleteFromHistory(user, 123L);
    assertEquals(200, response.getStatusCodeValue());
    verify(historiqueRepo).deleteById(123L);
  }

  /**
   * Teste la méthode {@link HistoriqueController#deleteFromHistory(Utilisateur,
   * Long)}. <p> Cette méthode teste la tentative de suppression d'un trajet de
   * l'historique d'un utilisateur qui n'est pas le propriétaire du trajet et
   * vérifie que l'accès est refusé.
   */
  @Test
  void testDeleteFromHistory_Forbidden() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);

    Utilisateur autreUser = new Utilisateur();
    autreUser.setId(2L);

    HistoriqueTrajet historique = new HistoriqueTrajet();
    historique.setId(123L);
    historique.setUtilisateur(autreUser);

    when(historiqueRepo.findById(123L)).thenReturn(Optional.of(historique));

    ResponseEntity<Void> response = controller.deleteFromHistory(user, 123L);
    assertEquals(403, response.getStatusCodeValue());
    verify(historiqueRepo, never()).deleteById(any());
  }
}

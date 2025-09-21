package group10.backendco2.controller;

import group10.backendco2.model.Trajet;
import group10.backendco2.repository.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Contrôleur REST pour gérer les trajets.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Créer un nouveau trajet</li>
 * </ul>
 */
@RestController
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController {
  /**
   * Références au dépôt pour les trajets.
   */
  private final TrajetRepository trajetRepository;
  /**
   * Crée un nouveau trajet.
   *
   * @param trajet le trajet à créer
   * @return le trajet créé
   */
  @PostMapping
  public ResponseEntity<Trajet> createTrajet(@RequestBody Trajet trajet) {
    Trajet saved = trajetRepository.save(trajet);
    return ResponseEntity.ok(saved);
  }
}

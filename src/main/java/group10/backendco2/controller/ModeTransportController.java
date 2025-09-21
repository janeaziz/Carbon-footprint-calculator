package group10.backendco2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Trajet;
import group10.backendco2.repository.TrajetRepository;
import group10.backendco2.service.GoogleMapService;
import group10.backendco2.service.ModeTransportService;
import group10.backendco2.service.TransportEmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer les modes de transport.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Obtenir tous les modes de transport</li>
 *   <li>Créer un nouveau mode de transport (Administrateur uniquement)</li>
 *   <li>Mettre à jour un mode de transport existant (Administrateur
 * uniquement)</li> <li>Supprimer un mode de transport par ID (Administrateur
 * uniquement)</li> <li>Rechercher les émissions de CO₂ en fonction de l'origine
 * et de la destination</li>
 * </ul>
 */
@RestController
@RequestMapping("/transports")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.75.53"})
public class ModeTransportController {
  
  /**
   * Service métier gérant les opérations liées aux trajets.
   */
  @Autowired private GoogleMapService googleMapService;
  /**
   * Références au dépôt pour les trajets.
   */
  @Autowired private TrajetRepository trajetRepository;
  /**
   * Service métier gérant les opérations liées aux modes de transport.
   */
  @Autowired private ModeTransportService service;
  /**
   * Service métier gérant les opérations liées aux émissions de transport.
   */
  @Autowired private TransportEmissionService emissionService;

  /**
   * Récupère tous les modes de transport disponibles dans la base.
   *
   * @return liste des objets {@link ModeTransport}
   */

  @Operation(summary = "Obtenir tous les modes de transport disponibles\n")
  @ApiResponse(responseCode = "200",
               description = "Liste des modes de transport\r\n")
  @GetMapping
  public List<ModeTransport>
  getAll() {
    return service.getAll();
  }

  /**
   * Crée un nouveau mode de transport (réservé aux administrateurs).
   *
   * @param modeTransport l'objet {@link ModeTransport} à créer
   * @return le transport enregistré
   */

  @Operation(
      summary =
          "Créer un nouveau mode de transport (Administrateur uniquement)\r\n")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Mode de transport créé\n")
                  ,
                      @ApiResponse(responseCode = "403",
                                   description =
                                       "Accès refusé (non administrateur)\r\n")
                })
  @PostMapping
  @PreAuthorize("hasAuthority('Admin')")
  public ModeTransport
  create(@RequestBody ModeTransport modeTransport) {
    return service.save(modeTransport);
  }
  /**
   * Met à jour un mode de transport existant par son identifiant (admin
   * uniquement).
   *
   * @param id identifiant du mode à modifier
   * @param updated les nouvelles données à enregistrer
   * @return l’objet {@link ModeTransport} mis à jour
   * @throws java.util.NoSuchElementException si le transport n’existe pas
   */

  @Operation(summary = "Mettre à jour un mode de transport existant "
                       + "(Administrateur uniquement)")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Mode de transport mis à jour")
                  ,
                      @ApiResponse(responseCode = "404",
                                   description =
                                       "Mode de transport non trouvé"),
                      @ApiResponse(responseCode = "403",
                                   description =
                                       "Accès refusé (non administrateur)")
                })
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('Admin')")
  public ModeTransport
  update(@PathVariable Long id, @RequestBody ModeTransport updated) {
    return service.update(id, updated).orElseThrow();
  }
  /**
   * Supprime un mode de transport par son identifiant (admin uniquement).
   *
   * @param id identifiant du mode à supprimer
   * @throws java.util.NoSuchElementException si le transport n’existe pas
   */
  @Operation(
      summary =
          "Supprimer un mode de transport par ID (Administrateur uniquement)")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Mode de transport supprimé")
                  ,
                      @ApiResponse(responseCode = "404",
                                   description =
                                       "Mode de transport non trouvé"),
                      @ApiResponse(responseCode = "403",
                                   description =
                                       "Accès refusé (non administrateur)")
                })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('Admin')")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
  /**
   * Recherche les émissions de CO₂ en fonction de l'origine et de la
   * destination via Google Maps.
   *
   * @param origine point de départ
   * @param destination point d'arrivée
   * @return liste des émissions de transport
   * @throws JsonProcessingException si une erreur se produit lors du traitement
   * des données JSON
   */
  @Operation(summary = "Rechercher les émissions de CO₂ en fonction de "
                       + "l'origine et de la destination via Google Maps")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description =
                                   "Liste des émissions de transport renvoyée")
                  ,
                      @ApiResponse(responseCode = "400",
                                   description = "Entrée invalide")
                })
  @GetMapping("/search")
  public List<TransportEmissionDto>
  searchEmissions(@RequestParam String origine,
                  @RequestParam String destination)
      throws JsonProcessingException {

    List<TransportEmissionDto> result =
        emissionService.calculateMultiModeEmissions(origine, destination);

    return result;
  }
  /**
   * Enregistre un trajet à partir des données de recherche.
   *
   * @param request le trajet à enregistrer
   * @return une réponse vide avec le statut HTTP 200 OK
   */
  @PostMapping("/search/save")
  public ResponseEntity<Void>
  saveTrajetFromSearch(@RequestBody Trajet request) {
    trajetRepository.save(request);
    return ResponseEntity.ok().build();
  }
}

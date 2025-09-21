package group10.backendco2.controller;

import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.service.ModeTransportService;
import group10.backendco2.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour les opérations d'administration.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>gérer les utilisateurs (liste, création, mise à jour,
 * suppression),</li> <li>gérer les modes de transport (liste, ajout, édition,
 * suppression).</li>
 * </ul>
 * Accès restreint aux administrateurs selon les méthodes.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminPanelController {

  /** Service métier pour la gestion des utilisateurs. */
  @Autowired private UtilisateurService userService;

  /** Service métier pour la gestion des modes de transport. */
  @Autowired private ModeTransportService transportService;

  /**
   * Récupère tous les utilisateurs de la base.
   *
   * @return une réponse contenant la liste des utilisateurs ou une erreur
   *     serveur
   */

  @GetMapping("/users")
  @Operation(summary = "Obtenir tous les utilisateurs")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Liste des utilisateurs récupérée")
                  ,
                      @ApiResponse(responseCode = "500",
                                   description = "Erreur serveur")
                })
  public ResponseEntity<List<Utilisateur>>
  getUsers() {
    try {
      List<Utilisateur> users = userService.findAll();
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Supprime un utilisateur en fonction de son identifiant.
   *
   * @param id identifiant de l'utilisateur à supprimer
   * @return 204 si supprimé, 404 si introuvable, ou 500 en cas d'erreur
   */

  @DeleteMapping("/users/{id}")
  @Operation(summary = "Supprimer un utilisateur")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "204",
                               description = "Utilisateur supprimé")
                  ,
                      @ApiResponse(responseCode = "404",
                                   description = "Utilisateur non trouvé"),
                      @ApiResponse(responseCode = "500",
                                   description = "Erreur serveur")
                })
  public ResponseEntity<Void>
  deleteUser(@PathVariable Long id) {
    try {
      if (!userService.findById(id).isPresent()) {
        return ResponseEntity.notFound().build();
      }
      userService.supprimer(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Met à jour partiellement les informations d’un utilisateur (nom, email,
   * rôle).
   *
   * @param id identifiant de l'utilisateur à mettre à jour
   * @param updates une map contenant les champs à modifier
   * @return l'utilisateur mis à jour ou un code d'erreur
   */

  @PutMapping("/users/{id}")
  @Operation(summary = "Mettre à jour un utilisateur")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Utilisateur mis à jour")
                  ,
                      @ApiResponse(responseCode = "400",
                                   description = "Données invalides"),
                      @ApiResponse(responseCode = "404",
                                   description = "Utilisateur non trouvé"),
                      @ApiResponse(responseCode = "500",
                                   description = "Erreur serveur")
                })
  public ResponseEntity<Utilisateur>
  updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    try {
      Optional<Utilisateur> userOptional = userService.findById(id);
      if (!userOptional.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      Utilisateur user = userOptional.get();

      if (updates.containsKey("email")) {
        user.setEmail((String)updates.get("email"));
      }
      if (updates.containsKey("nom")) {
        user.setNom((String)updates.get("nom"));
      }
      if (updates.containsKey("roles")) {
        String roleString = (String)updates.get("roles");
        try {
          Utilisateur.Role newRole = Utilisateur.Role.valueOf(roleString);
          user.setRole(newRole);
        } catch (IllegalArgumentException e) {
          return ResponseEntity.badRequest().body(null);
        }
      }

      Utilisateur updatedUser = userService.save(user);
      return ResponseEntity.ok(updatedUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Crée un nouvel utilisateur avec les informations fournies.
   *
   * @param user objet utilisateur à créer
   * @return l'utilisateur créé ou un code d'erreur
   */

  @PostMapping("/users")
  @Operation(summary = "Créer un nouvel utilisateur")
  @ApiResponses(
      value =
      {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé")
        , @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
      })
  public ResponseEntity<Utilisateur>
  createUser(@RequestBody Utilisateur user) {
    try {
      if (user.getEmail() == null || user.getMotDePasse() == null) {
        return ResponseEntity.badRequest().build();
      }
      Utilisateur createdUser = userService.save(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Récupère tous les modes de transport enregistrés.
   *
   * @return une réponse contenant la liste des transports ou une erreur serveur
   */

  @GetMapping("/transports")
  @Operation(summary = "Obtenir tous les transports")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Liste des transports récupérée")
                  ,
                      @ApiResponse(responseCode = "500",
                                   description = "Erreur serveur")
                })
  public ResponseEntity<List<ModeTransport>>
  getTransports() {
    try {
      List<ModeTransport> transports = transportService.getAll();
      return ResponseEntity.ok(transports);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Crée un nouveau mode de transport s’il est valide.
   *
   * @param transport objet {@link ModeTransport} à créer
   * @return le transport sauvegardé ou une erreur
   */

  @PostMapping("/transports")
  @Operation(summary = "Ajouter un nouveau transport")
  @ApiResponses(
      value =
      {
        @ApiResponse(responseCode = "201", description = "Transport créé")
        , @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
      })
  public ResponseEntity<ModeTransport>
  addTransport(@RequestBody ModeTransport transport) {
    try {
      if (transport.getNom() == null || transport.getNom().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      ModeTransport createdTransport = transportService.save(transport);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdTransport);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Supprime un mode de transport selon son identifiant.
   *
   * @param id identifiant du transport à supprimer
   * @return 204 si supprimé ou une erreur serveur
   */

  @DeleteMapping("/transports/{id}")
  @Operation(summary = "Supprimer un transport")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "204",
                               description = "Transport supprimé")
                  ,
                      @ApiResponse(responseCode = "500",
                                   description = "Erreur serveur")
                })
  public ResponseEntity<Void>
  deleteTransport(@PathVariable Long id) {
    try {
      transportService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Met à jour un mode de transport existant (admin uniquement).
   *
   * @param id identifiant du transport à mettre à jour
   * @param transport nouvelles données à enregistrer
   * @return le transport mis à jour ou une erreur
   */

  @PutMapping("/transports/{id}")
  @PreAuthorize("hasAuthority('Admin')")
  @Operation(summary = "Mettre à jour un transport")
  @ApiResponses(
      value =
      {
        @ApiResponse(responseCode = "200", description = "Transport mis à jour")
        , @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404",
                         description = "Transport non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
      })
  public ResponseEntity<ModeTransport>
  updateTransport(@PathVariable Long id, @RequestBody ModeTransport transport) {
    try {
      Optional<ModeTransport> existingTransport =
          transportService.update(id, transport);
      if (!existingTransport.isPresent()) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingTransport.get());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}

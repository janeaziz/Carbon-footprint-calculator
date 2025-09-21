package group10.backendco2.controller;

import group10.backendco2.dto.UserUpdateRequest;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Contrôleur REST pour gérer les utilisateurs.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Obtenir les informations de l'utilisateur actuel</li>
 *   <li>Modifier les informations de l'utilisateur actuel</li>
 *   <li>Obtenir un utilisateur par ID (Administrateur uniquement)</li>
 *   <li>Obtenir tous les utilisateurs</li>
 *   <li>Changer le rôle d'un utilisateur (Administrateur uniquement)</li>
 *   <li>Supprimer un utilisateur (Administrateur uniquement)</li>
 * </ul>
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Utilisateurs",
     description = "Opérations liées à la gestion des utilisateurs")
public class UtilisateurController {
  /**
   * Service métier gérant les opérations liées aux utilisateurs.
   */
  private final UtilisateurService userService;
  /**
   * Constructeur pour initialiser le service UtilisateurService.
   *
   * @param userService le service UtilisateurService à injecter
   */
  public UtilisateurController(UtilisateurService userService) {
    this.userService = userService;
  }
  /**
   * Récupère les informations de l'utilisateur actuel.
   *
   * @param currentUser l'utilisateur actuellement authentifié
   * @return les informations de l'utilisateur actuel
   */
  @GetMapping("/me")
  @Operation(summary = "Obtenir les informations de l'utilisateur actuel")
  public ResponseEntity<?>
  getCurrentUser(@AuthenticationPrincipal Utilisateur currentUser) {
    return ResponseEntity.ok(userService.toDto(currentUser));
  }
  /**
   * Modifie les informations de l'utilisateur actuel.
   *
   * @param currentUser l'utilisateur actuellement authentifié
   * @param request     la requête contenant les nouvelles informations de l'utilisateur
   * @return les informations mises à jour de l'utilisateur
   */
  @PutMapping("/me")
  @Operation(summary = "Modifier les informations de l'utilisateur actuel")
  public ResponseEntity<?>
  updateMe(@AuthenticationPrincipal Utilisateur currentUser,
           @RequestBody UserUpdateRequest request) {
    try {
      Utilisateur updatedUser =
          userService.updateUser(currentUser.getEmail(), request);
      return ResponseEntity.ok(userService.toDto(updatedUser));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  /**
   * Récupère un utilisateur par ID (Administrateur uniquement).
   *
   * @param id l'identifiant de l'utilisateur à récupérer
   * @return les informations de l'utilisateur correspondant à l'ID donné
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('Admin')")
  @Operation(
      summary = "Obtenir un utilisateur par ID",
      description = "Renvoie les informations de l'utilisateur correspondant "
                    + "à l'ID donné (Administrateur uniquement)")
  @ApiResponses(value =
                {
                  @ApiResponse(
                      responseCode = "200", description = "Utilisateur trouvé",
                      content = @Content(
                          mediaType = "application/json",
                          schema = @Schema(implementation = Utilisateur.class)))
                  ,
                      @ApiResponse(responseCode = "404",
                                   description = "Utilisateur non trouvé",
                                   content = @Content)
                })
  public ResponseEntity<?>
  getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> ResponseEntity.ok(userService.toDto(user)))
        .orElse(ResponseEntity.notFound().build());
  }
  /**
   * Récupère tous les utilisateurs.
   *
   * @return la liste de tous les utilisateurs enregistrés
   */
  @GetMapping
  @Operation(
      summary = "Obtenir tous les utilisateurs",
      description = "Renvoie la liste de tous les utilisateurs enregistrés")
  @ApiResponse(responseCode = "200",
               description = "Liste des utilisateurs renvoyée")
  public List<Utilisateur>
  getAll() {
    return userService.findAll(); // à sécuriser plus tard (admin uniquement)
  }
  /**
   * Change le rôle d'un utilisateur (Administrateur uniquement).
   *
   * @param id   l'identifiant de l'utilisateur dont le rôle doit être modifié
   * @param role le nouveau rôle à attribuer à l'utilisateur
   * @return les informations mises à jour de l'utilisateur
   */
  @PutMapping("/{id}/role")
  @PreAuthorize("hasAuthority('Admin')")
  @Operation(summary = "Changer le rôle d'un utilisateur",
             description = "Modifie le rôle de l'utilisateur spécifié "
                           + "(Administrateur uniquement)")
  @ApiResponses(value =
                {
                  @ApiResponse(responseCode = "200",
                               description = "Rôle mis à jour avec succès")
                  ,
                      @ApiResponse(responseCode = "400",
                                   description = "Valeur de rôle invalide")
                })
  public ResponseEntity<?>
  changeRole(@PathVariable Long id, @RequestParam String role) {
    try {
      Utilisateur.Role newRole = Utilisateur.Role.valueOf(role);
      Utilisateur updated = userService.modifierRole(id, newRole);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Rôle invalide");
    }
  }
  /**
   * Supprime un utilisateur (Administrateur uniquement).
   *
   * @param id l'identifiant de l'utilisateur à supprimer
   * @return une réponse indiquant le succès de l'opération
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('Admin')")
  @Operation(summary = "Supprimer un utilisateur",
             description = "Supprime l'utilisateur avec l'ID spécifié "
                           + "(Administrateur uniquement)")
  @ApiResponse(responseCode = "200",
               description = "Utilisateur supprimé avec succès")
  public ResponseEntity<?>
  deleteUser(@PathVariable Long id) {
    userService.supprimer(id);
    return ResponseEntity.ok().build();
  }
}

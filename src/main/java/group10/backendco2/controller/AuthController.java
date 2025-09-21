package group10.backendco2.controller;

import group10.backendco2.dto.LoginRequest;
import group10.backendco2.dto.SignupRequest;
import group10.backendco2.dto.UserResponseDto;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.service.JwtService;
import group10.backendco2.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour l'authentification des utilisateurs.
 *
 * Fournit deux endpoints publics :
 * <ul>
 *   <li><b>/auth/signup</b> : création d'un nouveau compte utilisateur</li>
 *   <li><b>/auth/login</b> : connexion avec génération de JWT</li>
 * </ul>
 */

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication",
     description = "Points de terminaison pour l'inscription et la connexion "
                   + "des utilisateurs\n")
public class AuthController {

  /**
   * Service métier gérant les opérations liées aux utilisateurs (inscription,
   * login).
   */
  private final UtilisateurService utilisateurService;

  /** Service chargé de la génération et validation des tokens JWT. */
  private final JwtService jwtService;

  /**
   * Constructeur de la classe AuthController.
   *
   * @param utilisateurService le service UtilisateurService
   * @param jwtService le service JwtService
   */
  public AuthController(UtilisateurService utilisateurService,
                        JwtService jwtService) {
    this.utilisateurService = utilisateurService;
    this.jwtService = jwtService;
  }

  /**
   * Enregistre un nouvel utilisateur à partir des données reçues.
   *
   * @param request les informations d'inscription (email, mot de passe, etc.)
   * @return un DTO contenant les informations du nouvel utilisateur
   */

  @Operation(summary = "Enregistrer un nouvel utilisateur\n",
             description =
                 "Crée un nouveau compte utilisateur à partir de l'email, du "
                 +
                 "mot de passe et d'informations utilisateur supplémentaires.")
  @ApiResponses(value =
                {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Utilisateur enregistré avec succès\r\n",
                      content =
                          @Content(mediaType = "application/json",
                                   schema = @Schema(implementation =
                                                        UserResponseDto.class)))
                  ,
                      @ApiResponse(responseCode = "400",
                                   description = "Requête invalide\r\n",
                                   content = @Content)
                })
  @PostMapping("/signup")
  public ResponseEntity<UserResponseDto>
  signup(@RequestBody SignupRequest request) {
    Utilisateur newUser = utilisateurService.inscrire(request);
    return ResponseEntity.ok(utilisateurService.toDto(newUser));
  }

  /**
   * Authentifie un utilisateur et génère un token JWT s'il est valide.
   *
   * @param request les identifiants de connexion
   * @return une réponse contenant le JWT et les données utilisateur (DTO)
   */

  @Operation(summary = "Authentifier un utilisateur\n",
             description = "Connecte un utilisateur et renvoie un jeton JWT "
                           + "si les identifiants sont valides.")
  @ApiResponses(value =
                {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Connexion réussie. Renvoie un jeton JWT "
                                    +
                                    "et les informations de l'utilisateur.\r\n",
                      content = @Content(mediaType = "application/json"))
                  ,
                      @ApiResponse(responseCode = "401",
                                   description = "Identifiants invalides\r\n",
                                   content = @Content)
                })
  @PostMapping("/login")
  public ResponseEntity<?>
  login(@RequestBody LoginRequest request) {
    Utilisateur user =
        utilisateurService.login(request.getEmail(), request.getMotDePasse());

    String token =
        jwtService.generateToken(user.getEmail(), user.getRole().name());

    return ResponseEntity.ok(
        Map.of("token", token, "user", utilisateurService.toDto(user)));
  }
}

package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.LoginRequest;
import group10.backendco2.dto.SignupRequest;
import group10.backendco2.dto.UserResponseDto;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.service.JwtService;
import group10.backendco2.service.UtilisateurService;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link AuthController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link AuthController} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class AuthControllerTest {
  /**
   * Instance de {@link AuthController} à tester.
   */
  private final UtilisateurService utilisateurService =
      mock(UtilisateurService.class);

  /**
   * Instance de {@link JwtService} à tester.
   */
  private final JwtService jwtService = mock(JwtService.class);

  /**
   * Instance de {@link AuthController} à tester.
   */
  private final AuthController controller =
      new AuthController(utilisateurService, jwtService);

  /**
   * Teste la méthode {@link AuthController#signup(SignupRequest)}.
   * <p>
   * Cette méthode teste l'inscription d'un nouvel utilisateur et vérifie que
   * la réponse est correcte.
   */
  @Test
  void testSignup() {
    SignupRequest request = new SignupRequest();
    request.nom = "Test";
    request.email = "test@co2.com";
    request.motDePasse = "password";

    Utilisateur mockUser = new Utilisateur();
    when(utilisateurService.inscrire(any())).thenReturn(mockUser);
    when(utilisateurService.toDto(mockUser))
        .thenReturn(new UserResponseDto(1L, "Test", "test@co2.com", "Visiteur",
                                        LocalDate.of(2025, 5, 17)));

    ResponseEntity<?> response = controller.signup(request);
    assertEquals(200, response.getStatusCodeValue());
    verify(utilisateurService).inscrire(any());
  }

  /**
   * Teste la méthode {@link AuthController#login(LoginRequest)}.
   * <p>
   * Cette méthode teste la connexion d'un utilisateur et vérifie que la
   * réponse est correcte.
   */
  @Test
  void testLogin() {
    LoginRequest request = new LoginRequest();
    request.setEmail("user@co2.com");
    request.setMotDePasse("password");

    Utilisateur mockUser = new Utilisateur();
    mockUser.setEmail("user@co2.com");
    mockUser.setRole(Utilisateur.Role.Visiteur);

    when(utilisateurService.login(any(), any())).thenReturn(mockUser);
    when(jwtService.generateToken(any(), any())).thenReturn("fake-jwt-token");
    when(utilisateurService.toDto(mockUser))
        .thenReturn(new UserResponseDto(1L, "User", "user@co2.com", "Visiteur",
                                        LocalDate.of(2025, 5, 17)));

    ResponseEntity<?> response = controller.login(request);
    assertEquals(200, response.getStatusCodeValue());

    Map<String, Object> body = (Map<String, Object>)response.getBody();
    assertTrue(body.containsKey("token"));
    assertTrue(body.containsKey("user"));
  }
}

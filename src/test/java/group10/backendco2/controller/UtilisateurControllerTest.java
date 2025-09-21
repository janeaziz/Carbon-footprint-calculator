package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.UserResponseDto;
import group10.backendco2.dto.UserUpdateRequest;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.service.UtilisateurService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link UtilisateurController}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link UtilisateurController}
 * pour s'assurer qu'elles fonctionnent correctement.
 */
class UtilisateurControllerTest {
  /**
   * Instance de {@link UtilisateurService} pour simuler le service des
   * utilisateurs.
   */
  private final UtilisateurService mockUserService =
      mock(UtilisateurService.class);
  /**
   * Instance de {@link UtilisateurController} à tester.
   */
  private final UtilisateurController controller =
      new UtilisateurController(mockUserService);

  /**
   * Teste la méthode {@link UtilisateurController#signup(SignupRequest)}.
   * <p>
   * Cette méthode teste l'inscription d'un nouvel utilisateur et vérifie que
   * la réponse est correcte.
   */
  @Test
  void testGetCurrentUser() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);
    user.setNom("Jane");
    user.setEmail("jane@co2.com");
    user.setRole(Utilisateur.Role.Visiteur);
    user.setDateInscription(LocalDate.now());

    when(mockUserService.toDto(user))
        .thenReturn(new UserResponseDto(1L, "Jane", "jane@co2.com", "Visiteur",
                                        LocalDate.now()));

    ResponseEntity<?> response = controller.getCurrentUser(user);
    assertEquals(200, response.getStatusCodeValue());
  }
  /**
   * Teste la méthode {@link UtilisateurController#updateMe(Utilisateur,
   * UserUpdateRequest)}.
   * <p>
   * Cette méthode teste la mise à jour des informations de l'utilisateur
   * actuel et vérifie que la réponse est correcte.
   */
  @Test
  void testUpdateMe_success() {
    Utilisateur user = new Utilisateur();
    user.setEmail("jane@co2.com");

    UserUpdateRequest req = new UserUpdateRequest();
    req.setName("Updated Name");
    req.setPassword("newpass");

    Utilisateur updated = new Utilisateur();
    updated.setId(1L);
    updated.setNom("Updated Name");
    updated.setEmail("jane@co2.com");
    updated.setDateInscription(LocalDate.now());
    updated.setRole(Utilisateur.Role.Visiteur);

    when(mockUserService.updateUser(eq("jane@co2.com"), any()))
        .thenReturn(updated);
    when(mockUserService.toDto(updated))
        .thenReturn(new UserResponseDto(1L, "Updated Name", "jane@co2.com",
                                        "Visiteur", LocalDate.now()));

    ResponseEntity<?> response = controller.updateMe(user, req);
    assertEquals(200, response.getStatusCodeValue());
  }
  /**
   * Teste la méthode {@link UtilisateurController#updateMe(Utilisateur,
   * UserUpdateRequest)}.
   * <p>
   * Cette méthode teste la mise à jour des informations de l'utilisateur
   * actuel et vérifie que la réponse est correcte.
   */
  @Test
  void testGetUser_found() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);
    user.setNom("Admin");
    user.setEmail("admin@co2.com");
    user.setRole(Utilisateur.Role.Admin);
    user.setDateInscription(LocalDate.now());

    when(mockUserService.findById(1L)).thenReturn(Optional.of(user));
    when(mockUserService.toDto(user))
        .thenReturn(new UserResponseDto(1L, "Admin", "admin@co2.com", "Admin",
                                        LocalDate.now()));

    ResponseEntity<?> response = controller.getUser(1L);
    assertEquals(200, response.getStatusCodeValue());
  }
  /**
   * Teste la méthode {@link UtilisateurController#updateMe(Utilisateur,
   * UserUpdateRequest)}.
   * <p>
   * Cette méthode teste la mise à jour des informations de l'utilisateur
   * actuel et vérifie que la réponse est correcte.
   */
  @Test
  void testGetUser_notFound() {
    when(mockUserService.findById(99L)).thenReturn(Optional.empty());
    ResponseEntity<?> response = controller.getUser(99L);
    assertEquals(404, response.getStatusCodeValue());
  }
  /**
   * Teste la méthode {@link UtilisateurController#getAll()}.
   * <p>
   * Cette méthode teste la récupération de tous les utilisateurs et
   * vérifie que la taille de la liste est correcte.
   */
  @Test
  void testGetAllUsers() {
    Utilisateur u = new Utilisateur();
    u.setEmail("all@co2.com");
    when(mockUserService.findAll()).thenReturn(List.of(u));
    List<Utilisateur> users = controller.getAll();
    assertEquals(1, users.size());
    assertEquals("all@co2.com", users.get(0).getEmail());
  }
  /**
   * Teste la méthode {@link UtilisateurController#changeRole(Long, String)}.
   * <p>
   * Cette méthode teste le changement de rôle d'un utilisateur et vérifie
   * que la réponse est correcte.
   */
  @Test
  void testChangeRole_success() {
    Utilisateur updated = new Utilisateur();
    updated.setId(2L);
    updated.setRole(Utilisateur.Role.Admin);
    when(mockUserService.modifierRole(2L, Utilisateur.Role.Admin))
        .thenReturn(updated);

    ResponseEntity<?> response = controller.changeRole(2L, "Admin");
    assertEquals(200, response.getStatusCodeValue());
  }
  /**
   * Teste la méthode {@link UtilisateurController#changeRole(Long, String)}.
   * <p>
   * Cette méthode teste le changement de rôle d'un utilisateur et vérifie
   * que la réponse est correcte.
   */
  @Test
  void testChangeRole_invalidRole() {
    ResponseEntity<?> response = controller.changeRole(2L, "INVALID");
    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Rôle invalide", response.getBody());
  }
  /**
   * Teste la méthode {@link UtilisateurController#deleteUser(Long)}.
   * <p>
   * Cette méthode teste la suppression d'un utilisateur et vérifie que
   * la réponse est correcte.
   */
  @Test
  void testDeleteUser() {
    doNothing().when(mockUserService).supprimer(3L);
    ResponseEntity<?> response = controller.deleteUser(3L);
    assertEquals(200, response.getStatusCodeValue());
  }
}

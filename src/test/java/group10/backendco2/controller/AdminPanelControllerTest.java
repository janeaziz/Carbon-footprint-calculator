package group10.backendco2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.model.Utilisateur.Role;
import group10.backendco2.service.ModeTransportService;
import group10.backendco2.service.UtilisateurService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test unitaire pour la classe {@link AdminPanelController}.
 * <p>
 * Cette classe teste le contrôleur du panneau d'administration pour s'assurer
 * qu'il fonctionne correctement dans différents scénarios.
 */

class AdminPanelControllerTest {

  /**
   * Instance de {@link AdminPanelController} à tester.
   */
  @InjectMocks private AdminPanelController controller;

  /**
   * Instance de {@link UtilisateurService} pour simuler le service des
   * utilisateurs.
   */
  @Mock private UtilisateurService userService;

  /**
   * Instance de {@link ModeTransportService} pour simuler le service des modes
   * de transport.
   */
  @Mock private ModeTransportService transportService;

  /**
   * Instance de {@link HttpServletRequest} pour simuler la requête HTTP.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // --- User Management Tests ---

  /**
   * Teste la méthode {@link AdminPanelController#getUsers()} pour s'assurer
   * qu'elle renvoie correctement la liste des utilisateurs.
   */
  @Test
  void getUsers_shouldReturnListOfUsers() {
    List<Utilisateur> users =
        Arrays.asList(new Utilisateur(), new Utilisateur());
    when(userService.findAll()).thenReturn(users);

    ResponseEntity<List<Utilisateur>> response = controller.getUsers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users, response.getBody());
  }

  /**
   * Teste la méthode {@link AdminPanelController#getUsers()} pour s'assurer
   * qu'elle renvoie une erreur 500 en cas d'exception.
   */
  @Test
  void getUsers_shouldReturn500OnException() {
    when(userService.findAll()).thenThrow(new RuntimeException());

    ResponseEntity<List<Utilisateur>> response = controller.getUsers();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteUser(Long)} pour
   * s'assurer qu'elle renvoie un code 204 si l'utilisateur existe.
   */
  @Test
  void deleteUser_shouldReturnNoContentIfUserExists() {
    Utilisateur user = new Utilisateur();
    when(userService.findById(1L)).thenReturn(Optional.of(user));

    ResponseEntity<Void> response = controller.deleteUser(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).supprimer(1L);
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteUser(Long)} pour
   * s'assurer qu'elle renvoie une erreur 404 si l'utilisateur n'existe pas.
   */
  @Test
  void deleteUser_shouldReturnNotFoundIfUserDoesNotExist() {
    when(userService.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Void> response = controller.deleteUser(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(userService, never()).supprimer(anyLong());
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteUser(Long)} pour
   * s'assurer qu'elle renvoie une erreur 500 en cas d'exception.
   */
  @Test
  void deleteUser_shouldReturn500OnException() {
    when(userService.findById(1L)).thenThrow(new RuntimeException());

    ResponseEntity<Void> response = controller.deleteUser(1L);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateUser(Long, Map)} pour
   * s'assurer qu'elle renvoie un code 200 si l'utilisateur est mis à jour avec
   * succès.
   */
  @Test
  void updateUser_shouldUpdateFieldsAndReturnUser() {
    Utilisateur user = new Utilisateur();
    user.setRole(Role.Admin);
    when(userService.findById(1L)).thenReturn(Optional.of(user));
    when(userService.save(any(Utilisateur.class)))
        .thenAnswer(i -> i.getArgument(0));

    Map<String, Object> updates = new HashMap<>();
    updates.put("email", "test@example.com");
    updates.put("nom", "Test");
    updates.put("roles", "Admin");

    ResponseEntity<Utilisateur> response = controller.updateUser(1L, updates);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("test@example.com", response.getBody().getEmail());
    assertEquals("Test", response.getBody().getNom());
    assertEquals(Role.Admin, response.getBody().getRole());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateUser(Long, Map)} pour
   * s'assurer qu'elle renvoie une erreur 400 si le rôle est invalide.
   */
  @Test
  void updateUser_shouldReturnBadRequestOnInvalidRole() {
    Utilisateur user = new Utilisateur();
    when(userService.findById(1L)).thenReturn(Optional.of(user));

    Map<String, Object> updates = new HashMap<>();
    updates.put("roles", "INVALID_ROLE");

    ResponseEntity<Utilisateur> response = controller.updateUser(1L, updates);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateUser(Long, Map)} pour
   * s'assurer qu'elle renvoie une erreur 400 si l'email est invalide.
   */
  @Test
  void updateUser_shouldReturnNotFoundIfUserDoesNotExist() {
    when(userService.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Utilisateur> response =
        controller.updateUser(1L, new HashMap<>());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateUser(Long, Map)} pour
   * s'assurer qu'elle renvoie une erreur 500 en cas d'exception.
   */
  @Test
  void updateUser_shouldReturn500OnException() {
    when(userService.findById(1L)).thenThrow(new RuntimeException());

    ResponseEntity<Utilisateur> response =
        controller.updateUser(1L, new HashMap<>());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#createUser(Utilisateur)} pour
   * s'assurer qu'elle renvoie un code 201 si l'utilisateur est créé avec
   * succès.
   */
  @Test
  void createUser_shouldReturnCreatedUser() {
    Utilisateur user = new Utilisateur();
    user.setEmail("test@example.com");
    user.setMotDePasse("password");
    when(userService.save(user)).thenReturn(user);

    ResponseEntity<Utilisateur> response = controller.createUser(user);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(user, response.getBody());
  }

  /**
   * Teste la méthode {@link AdminPanelController#createUser(Utilisateur)} pour
   * s'assurer qu'elle renvoie une erreur 400 si l'email est invalide.
   */
  @Test
  void createUser_shouldReturnBadRequestIfMissingFields() {
    Utilisateur user = new Utilisateur();
    user.setEmail(null);
    user.setMotDePasse("password");

    ResponseEntity<Utilisateur> response = controller.createUser(user);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#createUser(Utilisateur)} pour
   * s'assurer qu'elle renvoie une erreur 400 si le mot de passe est invalide.
   */
  @Test
  void createUser_shouldReturn500OnException() {
    Utilisateur user = new Utilisateur();
    user.setEmail("test@example.com");
    user.setMotDePasse("password");
    when(userService.save(user)).thenThrow(new RuntimeException());

    ResponseEntity<Utilisateur> response = controller.createUser(user);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  // --- Transport Management Tests ---

  /**
   * Teste la méthode {@link AdminPanelController#getTransports()} pour
   * s'assurer qu'elle renvoie correctement la liste des modes de transport.
   */
  @Test
  void getTransports_shouldReturnListOfTransports() {
    List<ModeTransport> transports =
        Arrays.asList(new ModeTransport(), new ModeTransport());
    when(transportService.getAll()).thenReturn(transports);

    ResponseEntity<List<ModeTransport>> response = controller.getTransports();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(transports, response.getBody());
  }

  /**
   * Teste la méthode {@link AdminPanelController#getTransports()} pour
   * s'assurer qu'elle renvoie une erreur 500 en cas d'exception.
   */
  @Test
  void getTransports_shouldReturn500OnException() {
    when(transportService.getAll()).thenThrow(new RuntimeException());

    ResponseEntity<List<ModeTransport>> response = controller.getTransports();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteTransport(Long)} pour
   * s'assurer qu'elle renvoie un code 204 si le mode de transport existe.
   */
  @Test
  void addTransport_shouldReturnCreatedTransport() {
    ModeTransport transport = new ModeTransport();
    transport.setNom("Bus");
    when(transportService.save(transport)).thenReturn(transport);

    ResponseEntity<ModeTransport> response = controller.addTransport(transport);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(transport, response.getBody());
  }

  /**
   * Teste la méthode {@link AdminPanelController#addTransport(ModeTransport)}
   * pour s'assurer qu'elle renvoie une erreur 400 si le nom du mode de
   * transport est manquant.
   */
  @Test
  void addTransport_shouldReturnBadRequestIfNameMissing() {
    ModeTransport transport = new ModeTransport();
    transport.setNom("");

    ResponseEntity<ModeTransport> response = controller.addTransport(transport);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#addTransport(ModeTransport)}
   * pour s'assurer qu'elle renvoie une erreur 400 si le mode de transport est
   * invalide.
   */
  @Test
  void addTransport_shouldReturn500OnException() {
    ModeTransport transport = new ModeTransport();
    transport.setNom("Bus");
    when(transportService.save(transport)).thenThrow(new RuntimeException());

    ResponseEntity<ModeTransport> response = controller.addTransport(transport);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteTransport(Long)} pour
   * s'assurer qu'elle renvoie un code 204 si le mode de transport existe.
   */
  @Test
  void deleteTransport_shouldReturnNoContent() {
    doNothing().when(transportService).delete(1L);

    ResponseEntity<Void> response = controller.deleteTransport(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(transportService).delete(1L);
  }

  /**
   * Teste la méthode {@link AdminPanelController#deleteTransport(Long)} pour
   * s'assurer qu'elle renvoie une erreur 404 si le mode de transport n'existe
   * pas.
   */
  @Test
  void deleteTransport_shouldReturn500OnException() {
    doThrow(new RuntimeException()).when(transportService).delete(1L);

    ResponseEntity<Void> response = controller.deleteTransport(1L);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateTransport(Long,
   * ModeTransport)} pour s'assurer qu'elle renvoie un code 200 si le mode de
   * transport est mis à jour avec succès.
   */
  @Test
  void updateTransport_shouldReturnUpdatedTransport() {
    ModeTransport transport = new ModeTransport();
    transport.setNom("Train");
    when(transportService.update(eq(1L), any(ModeTransport.class)))
        .thenReturn(Optional.of(transport));

    ResponseEntity<ModeTransport> response =
        controller.updateTransport(1L, transport);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(transport, response.getBody());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateTransport(Long,
   * ModeTransport)} pour s'assurer qu'elle renvoie une erreur 400 si le nom du
   * mode de transport est manquant.
   */
  @Test
  void updateTransport_shouldReturnNotFoundIfNotExists() {
    ModeTransport transport = new ModeTransport();
    when(transportService.update(eq(1L), any(ModeTransport.class)))
        .thenReturn(Optional.empty());

    ResponseEntity<ModeTransport> response =
        controller.updateTransport(1L, transport);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /**
   * Teste la méthode {@link AdminPanelController#updateTransport(Long,
   * ModeTransport)} pour s'assurer qu'elle renvoie une erreur 400 si le nom du
   * mode de transport est invalide.
   */
  @Test
  void updateTransport_shouldReturn500OnException() {
    ModeTransport transport = new ModeTransport();
    when(transportService.update(eq(1L), any(ModeTransport.class)))
        .thenThrow(new RuntimeException());

    ResponseEntity<ModeTransport> response =
        controller.updateTransport(1L, transport);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
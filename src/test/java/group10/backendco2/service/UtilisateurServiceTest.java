package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.dto.SignupRequest;
import group10.backendco2.dto.UserResponseDto;
import group10.backendco2.dto.UserUpdateRequest;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.model.Utilisateur.Role;
import group10.backendco2.repository.UtilisateurRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Test unitaire pour la classe {@link UtilisateurService}.
 * <p>
 * Vérifie les comportements attendus lors de l’inscription, connexion, mise à jour, 
 * suppression et transformation d’utilisateurs en DTO.
 */
class UtilisateurServiceTest {
  /**
   * Repository pour les utilisateurs.
   */
  private UtilisateurRepository utilisateurRepository;
  /**
   * Service pour les utilisateurs.
   */
  private PasswordEncoder passwordEncoder;
  /**
   * Service pour les utilisateurs.
   */
  private UtilisateurService utilisateurService;

  /**
   * Prépare le service UtilisateurService avant chaque test.
   *
   * Initialise le repository et injecte une instance fictive pour éviter les
   * appels réels.
   */
  @BeforeEach
  void setUp() {
    utilisateurRepository = mock(UtilisateurRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    utilisateurService =
        new UtilisateurService(utilisateurRepository, passwordEncoder);
  }
/**
 * Vérifie que l’inscription d’un nouvel utilisateur fonctionne si l’email n’est pas déjà utilisé.
 */
  @Test
  void inscrire_shouldSaveNewUser_whenEmailNotUsed() {
    SignupRequest request = new SignupRequest();
    request.nom = "John";
    request.email = "john@example.com";
    request.motDePasse = "password";

    when(utilisateurRepository.existsByEmail(request.email)).thenReturn(false);
    when(passwordEncoder.encode(request.motDePasse)).thenReturn("encodedPwd");
    Utilisateur savedUser = new Utilisateur();
    savedUser.setId(1L);
    when(utilisateurRepository.save(any(Utilisateur.class)))
        .thenReturn(savedUser);

    Utilisateur result = utilisateurService.inscrire(request);

    assertEquals(savedUser, result);
    verify(utilisateurRepository).save(any(Utilisateur.class));
  }

/**
 * Vérifie que l’inscription échoue si l’email est déjà présent en base.
 */
  @Test
  void inscrire_shouldThrow_whenEmailAlreadyUsed() {
    SignupRequest request = new SignupRequest();
    request.email = "john@example.com";
    when(utilisateurRepository.existsByEmail(request.email)).thenReturn(true);

    assertThrows(RuntimeException.class,
                 () -> utilisateurService.inscrire(request));
  }
/**
 * Vérifie que la méthode login retourne un utilisateur valide si les identifiants sont corrects.
 */
  @Test
  void login_shouldReturnUser_whenCredentialsAreCorrect() {
    String email = "john@example.com";
    String rawPwd = "password";
    String encodedPwd = "encodedPwd";
    Utilisateur user = new Utilisateur();
    user.setEmail(email);
    user.setMotDePasse(encodedPwd);

    when(utilisateurRepository.findByEmail(email))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPwd, encodedPwd)).thenReturn(true);

    Utilisateur result = utilisateurService.login(email, rawPwd);

    assertEquals(user, result);
  }
/**
 * Vérifie que la méthode login échoue si l’email n’est pas trouvé.
 */
  @Test
  void login_shouldThrow_whenEmailNotFound() {
    when(utilisateurRepository.findByEmail("notfound@example.com"))
        .thenReturn(Optional.empty());
    assertThrows(RuntimeException.class,
                 () -> utilisateurService.login("notfound@example.com", "pwd"));
  }

/**
 * Vérifie que la méthode login échoue si le mot de passe est incorrect.
 */
  @Test
  void login_shouldThrow_whenPasswordIncorrect() {
    String email = "john@example.com";
    Utilisateur user = new Utilisateur();
    user.setEmail(email);
    user.setMotDePasse("encodedPwd");

    when(utilisateurRepository.findByEmail(email))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "encodedPwd")).thenReturn(false);

    assertThrows(RuntimeException.class,
                 () -> utilisateurService.login(email, "wrong"));
  }
/**
 * Vérifie que la méthode findAll retourne bien tous les utilisateurs.
 */
  @Test
  void findAll_shouldReturnAllUsers() {
    List<Utilisateur> users = List.of(new Utilisateur(), new Utilisateur());
    when(utilisateurRepository.findAll()).thenReturn(users);

    List<Utilisateur> result = utilisateurService.findAll();

    assertEquals(users, result);
  }
/**
 * Vérifie que la méthode findById retourne l’utilisateur attendu.
 */
  @Test
  void findById_shouldReturnUser_whenExists() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);
    when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));

    Optional<Utilisateur> result = utilisateurService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(user, result.get());
  }
/**
 * Vérifie que supprimer() appelle bien deleteById().
 */
  @Test
  void supprimer_shouldCallDeleteById() {
    utilisateurService.supprimer(1L);
    verify(utilisateurRepository).deleteById(1L);
  }
/**
 * Vérifie que modifierRole change le rôle d’un utilisateur existant.
 */
  @Test
  void modifierRole_shouldUpdateRole() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);
    user.setRole(Role.Visiteur);
    when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
    when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(user);

    Utilisateur result = utilisateurService.modifierRole(1L, Role.Admin);

    assertEquals(Role.Admin, result.getRole());
    verify(utilisateurRepository).save(user);
  }

/**
 * Vérifie que la méthode toDto() transforme correctement un utilisateur en DTO.
 */
  @Test
  void toDto_shouldMapFieldsCorrectly() {
    Utilisateur user = new Utilisateur();
    user.setId(1L);
    user.setNom("Alice");
    user.setEmail("alice@example.com");
    user.setRole(Role.Visiteur);
    user.setDateInscription(LocalDate.of(2024, 1, 1));

    UserResponseDto dto = utilisateurService.toDto(user);

    assertEquals(1L, dto.getId());
    assertEquals("Alice", dto.getNom());
    assertEquals("alice@example.com", dto.getEmail());
    assertEquals("Visiteur", dto.getRole());
    assertEquals(LocalDate.of(2024, 1, 1), dto.getDateInscription());
  }
/**
 * Vérifie que updateUser met à jour le nom et le mot de passe si les champs sont valides.
 */
  @Test
  void updateUser_shouldUpdateNameAndPassword() {
    String email = "bob@example.com";
    Utilisateur user = new Utilisateur();
    user.setEmail(email);
    user.setNom("OldName");
    user.setMotDePasse("oldPwd");

    UserUpdateRequest req = new UserUpdateRequest();
    req.setName("NewName");
    req.setPassword("newPwd");

    when(utilisateurRepository.findByEmail(email))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.encode("newPwd")).thenReturn("encodedNewPwd");
    when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(user);

    Utilisateur result = utilisateurService.updateUser(email, req);

    assertEquals("NewName", result.getNom());
    assertEquals("encodedNewPwd", result.getMotDePasse());
  }
/**
 * Vérifie que updateUser ne modifie rien si les champs sont vides ou blancs.
 */
  @Test
  void updateUser_shouldNotUpdateFieldsIfBlank() {
    String email = "bob@example.com";
    Utilisateur user = new Utilisateur();
    user.setEmail(email);
    user.setNom("OldName");
    user.setMotDePasse("oldPwd");

    UserUpdateRequest req = new UserUpdateRequest();
    req.setName("   ");
    req.setPassword("");

    when(utilisateurRepository.findByEmail(email))
        .thenReturn(Optional.of(user));
    when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(user);

    Utilisateur result = utilisateurService.updateUser(email, req);

    assertEquals("OldName", result.getNom());
    assertEquals("oldPwd", result.getMotDePasse());
  }
/**
 * Vérifie que findByEmail retourne bien l’utilisateur recherché.
 */
  @Test
  void findByEmail_shouldReturnUser() {
    Utilisateur user = new Utilisateur();
    user.setEmail("test@example.com");
    when(utilisateurRepository.findByEmail("test@example.com"))
        .thenReturn(Optional.of(user));

    Optional<Utilisateur> result =
        utilisateurService.findByEmail("test@example.com");

    assertTrue(result.isPresent());
    assertEquals(user, result.get());
  }
/**
 * Vérifie que save encode le mot de passe s’il ne l’est pas encore.
 */
  @Test
  void save_shouldEncodePasswordIfNotEncoded() {
    Utilisateur user = new Utilisateur();
    user.setMotDePasse("plainPwd");
    when(passwordEncoder.encode("plainPwd")).thenReturn("encodedPwd");
    when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(user);

    utilisateurService.save(user);

    assertEquals("encodedPwd", user.getMotDePasse());
  }
/**
 * Vérifie que save ne ré-encode pas un mot de passe déjà encodé.
 */
  @Test
  void save_shouldNotEncodePasswordIfAlreadyEncoded() {
    Utilisateur user = new Utilisateur();
    user.setMotDePasse("$2a$alreadyEncoded");
    when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(user);

    utilisateurService.save(user);

    assertEquals("$2a$alreadyEncoded", user.getMotDePasse());
    verify(passwordEncoder, never()).encode(anyString());
  }
}
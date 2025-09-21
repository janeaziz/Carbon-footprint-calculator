package group10.backendco2.service;

import group10.backendco2.dto.SignupRequest;
import group10.backendco2.dto.UserResponseDto;
import group10.backendco2.dto.UserUpdateRequest;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.UtilisateurRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des utilisateurs.
 * <ul>
 *   <li>Inscription d'un nouvel utilisateur</li>
 *   <li>Connexion d'un utilisateur existant</li>
 *   <li>Récupération de tous les utilisateurs</li>
 *   <li>Suppression d'un utilisateur</li>
 *   <li>Mise à jour du rôle d'un utilisateur</li>
 * </ul>
 */
@Service
public class UtilisateurService {

  /**
   * Référentiel pour accéder aux données des utilisateurs.
   */
  private final UtilisateurRepository utilisateurRepository;

  /**
   * Encodeur de mot de passe pour sécuriser les mots de passe des utilisateurs.
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructeur de la classe UtilisateurService.
   *
   * @param utilisateurRepository le référentiel des utilisateurs
   * @param passwordEncoder l'encodeur de mot de passe
   */
  public UtilisateurService(UtilisateurRepository utilisateurRepository,
                            PasswordEncoder passwordEncoder) {
    this.utilisateurRepository = utilisateurRepository;
    this.passwordEncoder = passwordEncoder;
  }
  /**
   * Inscrit un nouvel utilisateur.
   *
   * @param request l'objet {@link SignupRequest} contenant les informations
   *     d'inscription
   * @return l'utilisateur inscrit
   */
  public Utilisateur inscrire(SignupRequest request) {
    if (utilisateurRepository.existsByEmail(request.email)) {
      throw new RuntimeException("Email déjà utilisé");
    }

    Utilisateur user = new Utilisateur();
    user.setNom(request.nom);
    user.setEmail(request.email);
    user.setMotDePasse(passwordEncoder.encode(request.motDePasse));
    user.setDateInscription(LocalDate.now());
    user.setRole(Utilisateur.Role.Visiteur); // par défaut

    return utilisateurRepository.save(user);
  }

  /**
   * Connecte un utilisateur existant.
   *
   * @param email l'email de l'utilisateur
   * @param motDePasse le mot de passe de l'utilisateur
   * @return l'utilisateur connecté
   */
  public Utilisateur login(String email, String motDePasse) {
    Utilisateur utilisateur =
        utilisateurRepository.findByEmail(email).orElseThrow(
            () -> new RuntimeException("Email introuvable"));

    if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
      throw new RuntimeException("Mot de passe incorrect");
    }

    return utilisateur;
  }

  /**
   * Récupère tous les utilisateurs.
   *
   * @return une liste de tous les utilisateurs
   */
  public List<Utilisateur> findAll() { return utilisateurRepository.findAll(); }

  /**
   * Récupère un utilisateur par son identifiant.
   *
   * @param id l'identifiant de l'utilisateur
   * @return l'utilisateur correspondant à l'identifiant
   */
  public Optional<Utilisateur> findById(Long id) {
    return utilisateurRepository.findById(id);
  }

  /**
   * Supprime un utilisateur par son identifiant.
   *
   * @param id l'identifiant de l'utilisateur à supprimer
   */
  public void supprimer(Long id) { utilisateurRepository.deleteById(id); }

  /**
   * Met à jour le rôle d'un utilisateur.
   *
   * @param id l'identifiant de l'utilisateur
   * @param nouveauRole le nouveau rôle de l'utilisateur
   * @return l'utilisateur mis à jour
   */
  public Utilisateur modifierRole(Long id, Utilisateur.Role nouveauRole) {
    Utilisateur user = utilisateurRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Utilisateur introuvable"));
    user.setRole(nouveauRole);
    return utilisateurRepository.save(user);
  }

  /**
   * Convertit un utilisateur en DTO de réponse.
   *
   * @param u l'utilisateur à convertir
   * @return le DTO de réponse de l'utilisateur
   */
  public UserResponseDto toDto(Utilisateur u) {
    return new UserResponseDto(u.getId(), u.getNom(), u.getEmail(),
                               u.getRole().name(), u.getDateInscription());
  }

  /**
   * Met à jour les informations d'un utilisateur.
   *
   * @param email l'email de l'utilisateur
   * @param request l'objet {@link UserUpdateRequest} contenant les nouvelles
   *     informations
   * @return l'utilisateur mis à jour
   */
  public Utilisateur updateUser(String email, UserUpdateRequest request) {
    Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(
        () -> new RuntimeException("Utilisateur non trouvé"));

    if (request.getName() != null && !request.getName().isBlank()) {
      user.setNom(request.getName());
    }

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setMotDePasse(passwordEncoder.encode(request.getPassword()));
    }
    Utilisateur saved = utilisateurRepository.save(user);
    return utilisateurRepository.save(user);
  }

  /**
   * Récupère un utilisateur par son email.
   *
   * @param email l'email de l'utilisateur
   * @return l'utilisateur correspondant à l'email
   */
  public Optional<Utilisateur> findByEmail(String email) {
    return utilisateurRepository.findByEmail(email);
  }

  /**
   * Enregistre un utilisateur (ajout ou mise à jour).
   *
   * @param utilisateur l'utilisateur à enregistrer
   * @return l'utilisateur enregistré
   */
  public Utilisateur save(Utilisateur utilisateur) {

    if (utilisateur.getMotDePasse() != null &&
        !utilisateur.getMotDePasse().startsWith("$2a$")) {
      utilisateur.setMotDePasse(
          passwordEncoder.encode(utilisateur.getMotDePasse()));
    }

    return utilisateurRepository.save(utilisateur);
  }
}

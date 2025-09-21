package group10.backendco2.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente un utilisateur de l'application, avec des informations
 * telles que son nom, son adresse e-mail, son mot de passe et son rôle.
 *
 * @param id identifiant unique de l'utilisateur
 * @param nom nom complet de l'utilisateur
 * @param email adresse e-mail unique utilisée comme identifiant de connexion
 * @param motDePasse mot de passe chiffré de l'utilisateur
 * @param dateInscription date d'inscription de l'utilisateur
 * @param role rôle de l'utilisateur dans l'application (Visiteur, Normal,
 *     Admin)
 */
@Getter
@Setter
@Entity
@Table(name = "utilisateur")
@Schema(description = "Représente un utilisateur de l'application")
public class Utilisateur {
  /**
   * Identifiant unique de l'utilisateur.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identifiant unique de l'utilisateur", example = "1")
  private Long id;
  /**
   * Nom complet de l'utilisateur.
   */
  @Schema(description = "Nom complet de l'utilisateur", example = "John Doe")
  private String nom;
  /**
   * Adresse e-mail unique utilisée comme identifiant de connexion.
   */
  @Column(unique = true)
  @Schema(description =
              "Adresse e-mail unique utilisée comme identifiant de connexion",
          example = "john.doe@example.com")
  private String email;
  /**
   * Mot de passe chiffré de l'utilisateur.
   */
  @Schema(description = "Mot de passe chiffré de l'utilisateur",
          example = "$2a$10$...")
  private String motDePasse;
  /**
   * Date d'inscription de l'utilisateur.
   */
  @Schema(description = "Date d'inscription de l'utilisateur",
          example = "2024-10-01")
  private LocalDate dateInscription;
  /**
   * Rôle de l'utilisateur dans l'application (Visiteur, Normal, Admin).
   */
  @Enumerated(EnumType.STRING)
  @Schema(description = "Rôle de l'utilisateur dans l'application",
          example = "Visiteur")
  private Role role = Role.Visiteur;
  /**
   * Enumération représentant les différents rôles d'utilisateur.
   */
  public enum Role { Visiteur, Normal, Admin }
}

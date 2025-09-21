package group10.backendco2.dto;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO représentant un utilisateur retourné par l’API.
 *
 * @param id identifiant unique de l’utilisateur
 * @param nom nom complet de l’utilisateur
 * @param email adresse email de l’utilisateur
 * @param role rôle attribué à l’utilisateur
 * @param dateInscription date d’inscription de l’utilisateur
 */
@Getter
@Setter
@Schema(description = "DTO représentant un utilisateur retourné par l’API")
public class UserResponseDto {
    /**
     * Identifiant unique de l’utilisateur.
     */
    @Schema(description = "Identifiant unique de l’utilisateur", example = "1")
    private Long id;
    /**
     * Nom complet de l’utilisateur.
     */
    @Schema(description = "Nom complet de l’utilisateur")
    private String nom;
    /**
     * Adresse email de l’utilisateur.
     */
    @Schema(description = "Adresse email de l’utilisateur")
    private String email;
    /**
     * Rôle attribué à l’utilisateur.
     */
    @Schema(description = "Rôle attribué à l’utilisateur")
    private String role;
    /**
     * Date d’inscription de l’utilisateur.
     */
    @Schema(description = "Date d’inscription de l’utilisateur")
    private LocalDate dateInscription;
    /**
     * Constructeur par défaut requis par Spring et Jackson.
     */
    public UserResponseDto(Long id, String nom, String email, String role, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.dateInscription = dateInscription;
    }
}

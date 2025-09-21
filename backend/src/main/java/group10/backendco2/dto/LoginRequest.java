package group10.backendco2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une requête de connexion contenant l'email et le mot de passe de l'utilisateur.
 *
 * @param email l'adresse email de l'utilisateur
 * @param motDePasse le mot de passe de l'utilisateur
 */
@Getter
@Setter
@Schema(description = "Requête de connexion contenant l'email et le mot de passe.")
public class LoginRequest {
    /**
     * Adresse email de l'utilisateur.
     */
    @Schema(description = "Adresse email de l'utilisateur", example = "user@example.com", required = true)
    private String email;
    /**
     * Mot de passe de l'utilisateur.
     */
    @Schema(description = "Mot de passe de l'utilisateur", example = "password123", required = true)
    private String motDePasse;

}

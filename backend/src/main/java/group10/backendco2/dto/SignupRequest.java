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
@Setter
@Getter
@Schema(description = "Requête d'inscription contenant le nom, l'email et le mot de passe.")
public class SignupRequest {
    /**
     * Nom complet de l'utilisateur.
     */
    @Schema(description = "Nom complet de l'utilisateur", example = "Jean Dupont", required = true)
    public String nom;
    /**
     * Adresse email de l'utilisateur.
     */
    @Schema(description = "Adresse email de l'utilisateur", example = "jeandupont@example.com", required = true)
    public String email;
    /**
     * Mot de passe de l'utilisateur.
     */
    @Schema(description = "Mot de passe de l'utilisateur", example = "motDePasseSecurise123", required = true)
    public String motDePasse;
}

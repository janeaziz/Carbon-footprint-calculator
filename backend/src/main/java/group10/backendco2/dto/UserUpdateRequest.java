package group10.backendco2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


/**
 * Représente une requête de mise à jour des informations utilisateur.
 *
 * @param name le nouveau nom de l'utilisateur
 * @param password le nouveau mot de passe
 */
@Getter
@Setter

@Schema(description = "Requête de mise à jour des informations utilisateur")
public class UserUpdateRequest {
    /**
     * Nouveau nom de l'utilisateur.
     */
    @Schema(description = "Nouveau nom de l'utilisateur", example = "Alice Dupont")
    private String name;
    /**
     * Nouveau mot de passe de l'utilisateur.
     */
    @Schema(description = "Nouveau mot de passe", example = "MotDePasse123!")
    private String password;

}

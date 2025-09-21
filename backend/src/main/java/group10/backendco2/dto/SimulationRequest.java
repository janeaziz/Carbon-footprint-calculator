package group10.backendco2.dto;

import group10.backendco2.common.AbstractSimulationData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
/**
 * Représente une requête de simulation de CO₂ contenant les données nécessaires
 * pour effectuer une simulation.
 *
 * @param utilisateurId l'ID de l'utilisateur ayant effectué la simulation
 */
@Getter
@Setter
@Schema(description = "Objet de requête pour créer une nouvelle simulation de CO₂")
public class SimulationRequest extends AbstractSimulationData {
    /**
     * ID de l'utilisateur ayant effectué la simulation.
     */
    @Schema(description = "ID de l'utilisateur ayant effectué la simulation", example = "1")
    private Long utilisateurId;
}

package group10.backendco2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * DTO représentant un historique de trajet enrichi avec les modes de transport et leurs émissions.
 *
 * @param id l'identifiant de l'historique
 * @param origine l'origine du trajet
 * @param destination la destination du trajet
 * @param date la date de réalisation du trajet
 * @param modes la liste des modes de transport et leurs émissions
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO enrichi de l'historique avec modes de transport")
public class HistoriqueDto {

    /**
     * Identifiant de l'historique.
     */
    @Schema(description = "ID de l'historique", example = "1")
    private Long id;
    /**
     * Origine du trajet.
     */
    @Schema(description = "Origine du trajet", example = "Lyon")
    private String origine;
    /**
     * Destination du trajet.
     */
    @Schema(description = "Destination du trajet", example = "Paris")
    private String destination;
    /**
     * Date de réalisation du trajet.
     */
    @Schema(description = "Date de réalisation du trajet", example = "2025-04-07")
    private Date date;
    /**
     * Liste des modes de transport et leurs émissions.
     */
    @Schema(description = "Liste des modes de transport et émissions")
    private List<TransportEmissionDto> modes;
    /**
     * Coût du trajet.
     */
    private String contrainte;
    
}

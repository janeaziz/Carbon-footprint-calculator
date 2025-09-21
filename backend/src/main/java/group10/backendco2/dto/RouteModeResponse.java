package group10.backendco2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une réponse contenant les informations d'un mode de transport sur
 * un itinéraire.
 *
 * @param mode le mode de transport utilisé
 * @param distanceKm la distance en kilomètres
 * @param estimatedTime le temps estimé de trajet
 * @param transitModes les modes de transport transit avec distances associées
 * @param transitStepLabels l'étiquette textuelle des étapes de transit
 * @param transitStepLabelsVerbose la liste détaillée des étapes de transit
 */
@Getter
@Setter
@Schema(description = "Réponse contenant les informations d'un mode de " +
                      "transport sur un itinéraire")
public class RouteModeResponse {
  /**
   * Mode de transport utilisé.
   */
  @Schema(description = "Mode de transport utilisé", example = "Train")
  private String mode;
  /**
   * Distance en kilomètres.
   */
  @Schema(description = "Distance en kilomètres", example = "425.5")
  private float distanceKm;
  /**
   * Temps estimé de trajet.
   */
  @Schema(description = "Temps estimé de trajet", example = "3h45")
  private String estimatedTime;
  /**
   * Modes de transport transit avec distances associées.
   */
  @Schema(description = "Modes de transport transit avec distances associées",
          example = "{\"Bus\": 2.5, \"Métro\": 3.0}")
  private Map<String, Float> transitModes;
  /**
   * Étiquette textuelle des étapes de transit.
   */
  @Schema(description = "Étiquette textuelle des étapes de transit",
          example = "Bus → Métro → Train")
  private String transitStepLabels;
  /**
   * Liste détaillée des étapes de transit.
   */
  @Schema(description = "Liste détaillée des étapes de transit",
          example = "[\"Prendre le Bus ligne 34\", \"Changer à Gare " +
                    "Centrale\", \"Prendre le Train TGV\"]")
  private List<String> transitStepLabelsVerbose;
  /**
   * Constructeur par défaut requis par Spring et Jackson.
   */
  public RouteModeResponse() {
    // No-arg constructor required by Spring and Jackson
  }
  /**
   * Constructeur avec paramètres pour initialiser les attributs de la classe.
   *
   * @param mode le mode de transport utilisé
   * @param distanceKm la distance en kilomètres
   * @param estimatedTime le temps estimé de trajet
   * @param transitModes les modes de transport transit avec distances associées
   */
  public RouteModeResponse(String mode, float distanceKm, String estimatedTime,
                           Map<String, Float> transitModes) {
    this.mode = mode;
    this.distanceKm = distanceKm;
    this.estimatedTime = estimatedTime;
    this.transitModes = transitModes;
  }
}

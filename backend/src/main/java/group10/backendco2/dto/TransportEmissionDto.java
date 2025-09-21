package group10.backendco2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO représentant les émissions de CO₂ pour un mode de transport donné.
 *
 * @param mode le mode de transport
 * @param co2 les émissions de CO₂ en grammes
 * @param distanceKm la distance en kilomètres
 * @param durationMinutes la durée estimée en minutes
 * @param subMode le sous-mode de transport avec détail de la distance
 * @param label la description détaillée de l’itinéraire
 * @param mapsUrl le lien direct vers l’itinéraire sur Google Maps
 * @param distanceLabel l'étiquette de distance à afficher
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    description =
        "DTO représentant les émissions de CO₂ pour un mode de transport donné")
public class TransportEmissionDto {

  /**
   * Constructeur par défaut requis par Spring et Jackson.
   */
  public TransportEmissionDto(String modeName, float emission, float distance,
                              float durationMinutes) {
    this.mode = modeName;
    this.co2 = emission;
    this.distanceKm = distance;
    this.durationMinutes = durationMinutes;
    this.subMode = null;
    this.label = null;
    this.mapsUrl = null;
    this.distanceLabel = formatDistance(distance);
  }

  /**
   * Constructeur avec sous-mode de transport.
   *
   * @param modeName nom du mode principal de transport
   * @param emission émissions de CO₂ en grammes
   * @param distance distance parcourue en kilomètres
   * @param durationMinutes durée estimée du trajet en minutes
   * @param subMode sous-mode utilisé dans l’itinéraire (ex. BUS, TRAM)
   */

  public TransportEmissionDto(String modeName, float emission, float distance,
                              float durationMinutes, String subMode) {
    this(modeName, emission, distance, durationMinutes);
    this.subMode = subMode;
  }
  /**
   * Constructeur avec sous-mode de transport et libellé d’itinéraire.
   *
   * @param modeName nom du mode principal de transport
   * @param emission émissions de CO₂ en grammes
   * @param distance distance parcourue en kilomètres
   * @param durationMinutes durée estimée du trajet en minutes
   * @param subMode sous-mode utilisé dans l’itinéraire
   * @param label description complète de l’itinéraire
   */

  public TransportEmissionDto(String modeName, float emission, float distance,
                              float durationMinutes, String subMode,
                              String label) {
    this(modeName, emission, distance, durationMinutes, subMode);
    this.label = label;
  }
  /**
   * Constructeur complet avec toutes les informations d’un trajet.
   *
   * @param modeName nom du mode principal de transport
   * @param emission émissions de CO₂ en grammes
   * @param distance distance parcourue en kilomètres
   * @param durationMinutes durée estimée du trajet en minutes
   * @param subMode sous-mode utilisé
   * @param label description de l’itinéraire
   * @param mapsUrl lien Google Maps vers l’itinéraire
   * @param distanceLabel étiquette lisible de la distance totale
   */

  public TransportEmissionDto(String modeName, float emission, float distance,
                              float durationMinutes, String subMode,
                              String label, String mapsUrl,
                              String distanceLabel) {
    this.mode = modeName;
    this.co2 = emission;
    this.distanceKm = distance;
    this.durationMinutes = durationMinutes;
    this.subMode = subMode;
    this.label = label;
    this.mapsUrl = mapsUrl;
    this.distanceLabel =
        distanceLabel != null ? distanceLabel : formatDistance(distance);
  }
  /**
   * Formate la distance en kilomètres avec deux décimales.
   *
   * @param d la distance à formater
   * @return la distance formatée
   */
  private String formatDistance(float d) {
    return String.format(Locale.US, "%.2f km", d)
        .replaceAll("\\s+", " ")
        .trim();
  }
  /**
   * Mode de transport utilisé.
   */
  @Schema(description = "Mode de transport", example = "Train")
  private String mode;
  /**
   * Émissions de CO₂ en grammes.
   */
  @Schema(description = "Émissions de CO₂ en grammes", example = "1540.0")
  private Float co2;
  /**
   * Distance en kilomètres.
   */
  @Schema(description = "Distance en kilomètres", example = "462.5")
  private Float distanceKm;
  /**
   * Temps estimé de trajet en minutes.
   */
  @Schema(description = "Durée estimée en minutes", example = "210.0")
  private Float durationMinutes;
  /**
   * Sous-mode de transport avec détail de la distance.
   */
  @Schema(description = "Sous-mode de transport avec détail de la distance",
          example = "BUS (12.5 km) + TRAM (5.3 km)")
  private String subMode;
  /**
   * Description détaillée de l’itinéraire.
   */
  @Schema(description = "Description détaillée de l’itinéraire, ex. : "
                        + "'MARCHE + TRAM + TRAIN'",
          example = "MARCHE + MÉTRO + TRAIN")
  private String label;
  /**
   * Lien direct vers l’itinéraire sur Google Maps.
   */
  @Schema(description = "Lien direct vers l’itinéraire sur Google Maps",
          example = "https://www.google.com/maps/dir/Lyon+3/Lyon+7")
  private String mapsUrl;
  /**
   * Étiquette de distance à afficher.
   */
  @Schema(description = "Étiquette de distance à afficher",
          example = "TRAM (5.30 km) + TRAIN (100.15 km)")
  private String distanceLabel;
  /**
   * Consommation d’énergie totale pour ce trajet.
   */
  @Schema(description = "Consommation d’énergie totale pour ce trajet",
          example = "5.20")
  private Float consommationEnergie;
  /**
   * Unité de consommation d’énergie (L ou kWh).
   */
  @Schema(description = "Unité de consommation d’énergie (L ou kWh)",
          example = "L")
  private String unite;
  /**
   * Coût estimé du trajet.
   */
  @Schema(description = "Coût estimé du trajet", example = "7.85")
  private Float prixEstime;
}

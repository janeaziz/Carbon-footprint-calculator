package group10.backendco2.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
/**
 * Classe abstraite commune aux entités liées à la simulation de trajets.
 *
 * Contient les champs de base partagés (origine, destination, mode de
 * transport, etc.) entre plusieurs entités de simulation, pour favoriser la
 * réutilisation.
 *
 * Annotée avec {@code @MappedSuperclass} pour être héritée par des entités JPA.
 */

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractSimulationData {
  /** Point de départ du trajet simulé. */
  @Schema(description = "Point de départ du trajet", example = "Lyon")
  protected String origine;

  /** Point d’arrivée du trajet simulé. */
  @Schema(description = "Destination du trajet", example = "Paris")
  protected String destination;

  /** Nom du mode de transport utilisé (ex : Train, Voiture, etc.). */
  @Schema(description = "Mode de transport utilisé", example = "Train")
  protected String modeTransport;

  /**
   * Fréquence à laquelle le trajet est effectué (ex : quotidienne,
   * hebdomadaire).
   */
  @Schema(description = "Fréquence du trajet", example = "quotidienne")
  protected String frequency;

  /** Durée de la simulation en jours. */
  @Schema(description = "Durée de la simulation en jours", example = "30")
  protected int duration;
  
  /** Émissions totales estimées de CO₂ sur la période spécifiée. */
  @Schema(description = "Émission totale estimée de CO₂", example = "6543.25")
  protected float totalEmission;
}

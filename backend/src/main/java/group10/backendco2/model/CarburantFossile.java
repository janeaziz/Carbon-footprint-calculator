package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente un type de carburant fossile avec ses propriétés associées comme
 * le prix et la densité.
 *
 * @param type le type de carburant fossile (ex. : Diesel, Essence)
 * @param prix le prix du carburant fossile par litre
 * @param densite la densité du carburant fossile en kg/L
 */
@Entity
@Table(name = "carburantfossile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un type de carburant fossile avec ses " +
                      "propriétés associées comme le prix et la densité.")
public class CarburantFossile {
  /**
   * Type de carburant fossile (ex. : Diesel, Essence).
   */
  @Id
  @Schema(description = "Type de carburant fossile (ex. : Diesel, Essence)",
          example = "Diesel")
  private String type;
  /**
   * Prix du carburant fossile par litre.
   */
  @Schema(description = "Prix du carburant fossile par litre", example = "1.75")
  private Float prix;
  /**
   * Densité du carburant fossile en kg/L.
   */
  @Schema(description = "Densité du carburant fossile en kg/L",
          example = "0.84")
  private Float densite;
  /**
   * Source d'énergie associée à ce carburant.
   */
  @ManyToOne
  @JoinColumn(name = "sourceenergie_id")
  @Schema(description = "Source d'énergie associée à ce carburant")
  private SourceEnergie sourceEnergie;
}

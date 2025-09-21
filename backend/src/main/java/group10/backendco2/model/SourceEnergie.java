package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une source d'énergie utilisée par les modes de transport, avec
 * les valeurs d’émission de CO₂ associées.
 *
 * @param id identifiant unique de la source d'énergie
 * @param nom nom de la source d'énergie
 * @param emission facteur d’émission en grammes de CO₂ par unité (ex. : par
 *     litre ou par kWh)
 */
@Entity
@Table(name = "sourceenergie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description =
            "Représente une source d'énergie utilisée par les modes de "
            + "transport, avec les valeurs d’émission de CO₂ associées.")
public class SourceEnergie {
  /**
   * Identifiant unique de la source d'énergie.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identifiant unique de la source d'énergie",
          example = "1")
  private Long id;

  /**
   * Nom de la source d'énergie.
   */
  @Schema(description = "Nom de la source d'énergie", example = "Diesel")
  private String nom;

  /**
   * Facteur d’émission en grammes de CO₂ par unité (ex. : par litre ou par
   * kWh).
   */
  @Schema(description = "Facteur d’émission en grammes de CO₂ par unité (ex. "
                        + ": par litre ou par kWh)",
          example = "2390.0")
  private Float emission;
}

package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente un mode de transport spécifique, comme une voiture, un bus ou une
 * trottinette électrique.
 *
 * @param id identifiant unique du mode de transport
 * @param nom nom du mode de transport
 * @param consommationMoyenne consommation moyenne (par exemple en L/100km ou
 *     kWh/100km)
 * @param capacite nombre maximal de passagers que le mode de transport peut
 *     transporter
 * @param typeTransport type de transport, par exemple routier, ferroviaire ou
 *     aérien
 * @param sourceEnergie source d'énergie utilisée par le mode de transport, par
 *     exemple électricité ou diesel
 * @param tarifPublicParKm coût public moyen par kilomètre (en euros)
 */
@Entity
@Table(name = "modetransport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un mode de transport spécifique, comme une "
                      + "voiture, un bus ou une trottinette électrique.")
public class ModeTransport {
  /**
   * Identifiant unique du mode de transport.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identifiant unique du mode de transport",
          example = "1")
  private Long id;
  /**
   * Nom du mode de transport.
   */
  @Schema(description = "Nom du mode de transport",
          example = "Voiture électrique")
  private String nom;
  /**
   * Consommation moyenne (par exemple en L/100km ou kWh/100km).
   */
  @Schema(description =
              "Consommation moyenne (par exemple en L/100km ou kWh/100km)",
          example = "15.5")
  private Float consommationMoyenne;
  /**
   * Nombre maximal de passagers que le mode de transport peut transporter.
   */
  @Schema(description = "Nombre maximal de passagers que le mode de "
                        + "transport peut transporter",
          example = "5")
  private Integer capacite;
  /**
   * Type de transport, par exemple routier, ferroviaire ou aérien.
   */
  @ManyToOne
  @JoinColumn(name = "typetransport_id")
  @Schema(description =
              "Type de transport, par exemple routier, ferroviaire ou aérien")
  private TypeTransport typeTransport;
  /**
   * Source d'énergie utilisée par le mode de transport, par exemple électricité
   * ou diesel.
   */
  @ManyToOne
  @JoinColumn(name = "sourceenergie_id")
  @Schema(description = "Source d'énergie utilisée par le mode de transport, "
                        + "par exemple électricité ou diesel")
  private SourceEnergie sourceEnergie;
  /**
   * Coût public moyen par kilomètre (en euros).
   */
  @Column(name = "tarifpublicparkm")
  @Schema(description = "Coût public moyen par kilomètre (en euros)",
          example = "0.15")
  private Float tarifPublicParKm;
}

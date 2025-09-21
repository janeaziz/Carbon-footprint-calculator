package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
 * Représente une catégorie de transport, telle qu'individuel, collectif, etc.
 *
 * @param id identifiant unique du type de transport
 * @param nom nom du type de transport
 * @param description description détaillée du type de transport
 */
@Entity
@Table(name = "typetransport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente une catégorie de transport, telle "
                      + "qu'individuel, collectif, etc.")
public class TypeTransport {
  /**
   * Identifiant unique du type de transport.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identifiant unique du type de transport",
          example = "1")
  private Long id;
  /**
   * Nom du type de transport.
   */
  @Schema(description = "Nom du type de transport", example = "Collectif")
  private String nom;
  /**
   * Description détaillée du type de transport.
   */
  @Column(columnDefinition = "TEXT")
  @Schema(description = "Description détaillée du type de transport",
          example = "Inclut les bus, les trams et les métros")
  private String description;
}

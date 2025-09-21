package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Représente un trajet effectué dans le passé par un utilisateur, avec la date
 * de réalisation et les liens vers l'utilisateur et le trajet.
 *
 * @param id l'identifiant unique du trajet historique
 * @param dateRealisation la date à laquelle le trajet a été effectué
 * @param utilisateur l'utilisateur ayant effectué le trajet
 * @param trajet les informations sur le trajet (origine, destination, etc.)
 */
@Entity
@Table(name = "historiquetrajet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un trajet effectué dans le passé par un " +
                      "utilisateur, avec la date de réalisation et les liens " +
                      "vers l'utilisateur et le trajet.")
public class HistoriqueTrajet {
  /**
   * Identifiant unique du trajet historique.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identifiant unique du trajet historique",
          example = "1")
  private Long id;
  /**
   * Date à laquelle le trajet a été effectué.
   */
  @Schema(description = "Date à laquelle le trajet a été effectué",
          example = "2024-04-01")
  private Date dateRealisation;
  /**
   * Utilisateur ayant effectué le trajet.
   */
  @ManyToOne
  @JoinColumn(name = "utilisateur_id")
  @Schema(description = "Utilisateur ayant effectué le trajet")
  private Utilisateur utilisateur;
  /**
   * Informations sur le trajet (origine, destination, etc.).
   */
  @ManyToOne
  @JoinColumn(name = "trajet_id")
  @Schema(description =
              "Informations sur le trajet (origine, destination, etc.)")
  private Trajet trajet;
}

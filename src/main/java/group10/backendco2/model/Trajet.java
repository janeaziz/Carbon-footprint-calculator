package group10.backendco2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un trajet entre deux lieux, incluant la distance et d’éventuelles
 * contraintes de transport.
 *
 * @param id identifiant unique du trajet
 * @param origine lieu de départ du trajet
 * @param destination lieu d'arrivée du trajet
 * @param distance distance du trajet en kilomètres
 * @param contrainte contraintes ou remarques optionnelles liées au trajet
 * @param co2 émission totale de CO₂ pour ce trajet
 */
@Entity
@Table(name = "trajet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un trajet entre deux lieux, incluant la distance et d’éventuelles contraintes de transport.")
public class Trajet {
    /**
     * Identifiant unique du trajet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du trajet", example = "1")
    private Long id;
    /**
     * Lieu de départ du trajet.
     */
    @Schema(description = "Lieu de départ du trajet", example = "Lyon")
    private String origine;
    /**
     * Lieu d'arrivée du trajet.
     */
    @Schema(description = "Lieu d'arrivée du trajet", example = "Paris")
    private String destination;
    /**
     * Distance du trajet en kilomètres.
     */
    @Schema(description = "Distance du trajet en kilomètres", example = "465.5")
    private Float distance;
    /**
     * Contraintes ou remarques optionnelles liées au trajet.
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "Contraintes ou remarques optionnelles liées au trajet", example = "Durée estimée : 5h 20m")
    private String contrainte;
    /**
     * Émission totale de CO₂ pour ce trajet.
     */
    @Column(name = "co2emissions")
    @Schema(description = "Émission totale de CO₂ pour ce trajet", example = "123.45")
    private Float co2;
    /**
     * Ensemble des modes de transport associés à ce trajet.
     */
    @ManyToMany
    @JoinTable(
        name = "utilisationmodetransport",
        joinColumns = @JoinColumn(name = "trajet_id"),
        inverseJoinColumns = @JoinColumn(name = "modetransport_id")
    )
    @Schema(description = "Ensemble des modes de transport associés à ce trajet")
    private Set<ModeTransport> modesTransport = new HashSet<>();
    
}

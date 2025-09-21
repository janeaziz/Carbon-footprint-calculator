package group10.backendco2.model;

import group10.backendco2.common.AbstractSimulationData;
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
 * Représente une simulation d'émissions de CO₂ basée sur un trajet.
 *
 * @param id identifiant unique de la simulation
 * @param dateSimulation date à laquelle la simulation a été effectuée
 * @param utilisateur utilisateur ayant effectué la simulation
 */
@Entity
@Table(name = "simulation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description =
            "Représente une simulation d'émissions de CO₂ basée sur un trajet")
public class Simulation extends AbstractSimulationData {
  /**
   * Identifiant unique de la simulation.
   */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;

  /**
   * Date à laquelle la simulation a été effectuée.
   */
  private Date dateSimulation;

  /**
   * Id unique d'un utilisateur ayant effectué la simulation.
   */
  @ManyToOne
  @JoinColumn(name = "utilisateur_id")
  private Utilisateur utilisateur;


  /**
   * Constructeur de la classe Simulation.
   *
   * @param id l'identifiant unique de la simulation
   * @param dateSimulation la date à laquelle la simulation a été effectuée
   * @param origine l'origine du trajet
   * @param destination la destination du trajet
   * @param modeTransport le mode de transport utilisé
   * @param frequency la fréquence d'utilisation du mode de transport
   * @param duration la durée du trajet en minutes
   * @param totalEmission les émissions totales de CO₂ pour le trajet
   * @param utilisateur l'utilisateur ayant effectué la simulation
   */
  public Simulation(Long id, Date dateSimulation, String origine,
                    String destination, String modeTransport, String frequency,
                    int duration, float totalEmission,
                    Utilisateur utilisateur) {
    this.id = id;
    this.dateSimulation = dateSimulation;
    this.origine = origine;
    this.destination = destination;
    this.modeTransport = modeTransport;
    this.frequency = frequency;
    this.duration = duration;
    this.totalEmission = totalEmission;
    this.utilisateur = utilisateur;
  }
  /**
   * Constructeur de la classe Simulation sans identifiant.
   *
   * @param dateSimulation la date à laquelle la simulation a été effectuée
   * @param origine l'origine du trajet
   * @param destination la destination du trajet
   * @param modeTransport le mode de transport utilisé
   * @param frequency la fréquence d'utilisation du mode de transport
   * @param duration la durée du trajet en minutes
   * @param totalEmission les émissions totales de CO₂ pour le trajet
   * @param utilisateur l'utilisateur ayant effectué la simulation
   */
  public Simulation(Date dateSimulation, String origine, String destination,
                    String modeTransport, String frequency, int duration,
                    float totalEmission, Utilisateur utilisateur) {
    this.dateSimulation = dateSimulation;
    this.origine = origine;
    this.destination = destination;
    this.modeTransport = modeTransport;
    this.frequency = frequency;
    this.duration = duration;
    this.totalEmission = totalEmission;
    this.utilisateur = utilisateur;
  }
}

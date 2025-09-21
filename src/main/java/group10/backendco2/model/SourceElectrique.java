package group10.backendco2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une source d'énergie électrique avec son prix par kWh.
 *
 * @param sourceEnergieId identifiant unique de la source d'énergie
 * @param prixKWH prix par kWh
 * @param sourceEnergie référence à la source d'énergie associée
 */
@Getter
@Setter
@Entity
@Table(name = "sourceelectrique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceElectrique {
    /**
     * Identifiant unique de la source d'énergie.
     */
    @Id
    @Column(name = "sourceenergie_id")
    private Long sourceEnergieId;
    /**
     * Prix par kWh.
     */
    private float prixKWH;
    /**
     * Source d'énergie associée à cette source électrique.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "sourceenergie_id")  
    private SourceEnergie sourceEnergie;

}

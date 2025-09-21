package group10.backendco2.repository;

import group10.backendco2.model.ModeTransport;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Interface de repository pour l'entité ModeTransport.
 * Permet d'effectuer des opérations CRUD sur la base de données.
 */
public interface ModeTransportRepository
    extends JpaRepository<ModeTransport, Long> {

  /**
   * Trouve tous les modes de transport associés à un nom donné.
   *
   * @param nom le nom du mode de transport
   * @return une liste d'objets ModeTransport
   */
  @Query("SELECT m FROM ModeTransport m WHERE LOWER(m.nom) LIKE " +
         "LOWER(CONCAT('%', :nom, '%'))")
  List<ModeTransport>
  findByNomApprox(@Param("nom") String nom);
}

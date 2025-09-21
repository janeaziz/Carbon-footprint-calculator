package group10.backendco2.service;

import group10.backendco2.model.ModeTransport;
import group10.backendco2.repository.ModeTransportRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des modes de transport.
 * <ul>
 *   <li>Récupération de tous les modes de transport</li>
 *   <li>Ajout d'un nouveau mode de transport</li>
 *   <li>Mise à jour d'un mode de transport existant</li>
 *   <li>Suppression d'un mode de transport</li>
 * </ul>
 */
@Service
public class ModeTransportService {

  /**
   * Référentiel pour accéder aux données des modes de transport.
   */
  @Autowired private ModeTransportRepository modeTransportRepository;
  /**
   * Récupère tous les modes de transport.
   *
   * @return une liste de tous les modes de transport
   */
  public List<ModeTransport> getAll() {
    return modeTransportRepository.findAll();
  }

  /**
   * Enregistre un nouveau mode de transport dans la base de données.
   *
   * @param modeTransport l'objet {@link ModeTransport} à sauvegarder
   * @return l'objet {@link ModeTransport} sauvegardé
   */

  public ModeTransport save(ModeTransport modeTransport) {
    return modeTransportRepository.save(modeTransport);
  }

  /**
   * Met à jour un mode de transport existant.
   *
   * @param id l'identifiant du mode de transport à mettre à jour
   * @param updated l'objet {@link ModeTransport} contenant les nouvelles
   *     informations
   * @return l'objet {@link ModeTransport} mis à jour, ou un {@link
   *     Optional#empty()} si le mode de transport n'existe pas
   */
  public Optional<ModeTransport> update(Long id, ModeTransport updated) {
    return modeTransportRepository.findById(id).map(existing -> {
      existing.setNom(updated.getNom());
      existing.setConsommationMoyenne(updated.getConsommationMoyenne());
      existing.setCapacite(updated.getCapacite());
      existing.setTypeTransport(updated.getTypeTransport());
      existing.setSourceEnergie(updated.getSourceEnergie());
      return modeTransportRepository.save(existing);
    });
  }
  /**
   * Supprime un mode de transport par son identifiant.
   *
   * @param id l'identifiant du mode de transport à supprimer
   */
  public void delete(Long id) { modeTransportRepository.deleteById(id); }
}

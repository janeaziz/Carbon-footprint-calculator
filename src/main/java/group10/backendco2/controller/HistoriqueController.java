package group10.backendco2.controller;

import group10.backendco2.dto.HistoriqueDto;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.HistoriqueTrajet;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.Trajet;
import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.HistoriqueTrajetRepository;
import group10.backendco2.repository.TrajetRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Contrôleur REST pour gérer l'historique des trajets d'un utilisateur.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Ajouter un trajet à l'historique</li>
 *   <li>Obtenir l'historique de l'utilisateur connecté</li>
 *   <li>Supprimer un trajet de l'historique</li>
 * </ul>
 */
@RestController
@RequestMapping("/history")
public class HistoriqueController {

    /**
     * Références aux dépôts pour l'historique des trajets et les trajets.
     */
    private final HistoriqueTrajetRepository historiqueRepository;

    /**
     * Références au dépôt pour les trajets.
     */
    private final TrajetRepository trajetRepository;


    /**
     * Constructeur de la classe HistoriqueController.
     *
     * @param historiqueRepository le dépôt HistoriqueTrajetRepository
     * @param trajetRepository le dépôt TrajetRepository
     */
    public HistoriqueController(HistoriqueTrajetRepository historiqueRepository,
                                TrajetRepository trajetRepository) {
        this.historiqueRepository = historiqueRepository;
        this.trajetRepository = trajetRepository;
    }

    /**
     * Ajoute un trajet à l'historique de l'utilisateur connecté.
     *
     * @param currentUser l'utilisateur connecté
     * @param trajetId l'identifiant du trajet à ajouter
     * @return le trajet historique ajouté
     */
    @PostMapping
    @Operation(summary = "Ajouter un trajet à l'historique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique ajouté")
    })
    public HistoriqueTrajet addToHistory(
            @AuthenticationPrincipal Utilisateur currentUser,
            @RequestParam Long trajetId) {

        Trajet trajet = trajetRepository.findByIdWithModes(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet introuvable"));

        HistoriqueTrajet historique = new HistoriqueTrajet();
        historique.setUtilisateur(currentUser);
        historique.setTrajet(trajet);
        historique.setDateRealisation(new Date());

        return historiqueRepository.save(historique);
    }

    /**
     * Obtient l'historique des trajets de l'utilisateur connecté.
     *
     * @param currentUser l'utilisateur connecté
     * @return la liste des trajets historiques de l'utilisateur
     */
    @GetMapping
    @Operation(summary = "Obtenir l'historique de l'utilisateur connecté")
    public List<HistoriqueDto> getHistory(@AuthenticationPrincipal Utilisateur currentUser) {

        List<HistoriqueTrajet> historique = historiqueRepository.findByUtilisateurId(currentUser.getId());

        return historique.stream().map(h -> {
            Trajet trajet = trajetRepository.findByIdWithModes(h.getTrajet().getId())
                    .orElseThrow(() -> new RuntimeException("Trajet introuvable"));

            List<TransportEmissionDto> modes = new ArrayList<>();
            for (ModeTransport mt : trajet.getModesTransport()) {
                if (mt.getSourceEnergie() != null && mt.getTypeTransport() != null) {
                    float co2 = mt.getSourceEnergie().getEmission() * mt.getConsommationMoyenne();
                    modes.add(new TransportEmissionDto(
                            mt.getNom(),
                            co2,
                            trajet.getDistance().floatValue(),
                            0f,
                            null,
                            mt.getTypeTransport().getNom()
                    ));
                }
            }

            return new HistoriqueDto(
                    h.getId(),
                    trajet.getOrigine(),
                    trajet.getDestination(),
                    h.getDateRealisation(),
                    modes,
                    trajet.getContrainte() 
            );

        }).collect(Collectors.toList());
    }

    /**
     * Supprime un trajet de l'historique de l'utilisateur connecté.
     *
     * @param currentUser l'utilisateur connecté
     * @param id l'identifiant du trajet à supprimer
     * @return une réponse vide
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un trajet de l'historique")
    public ResponseEntity<Void> deleteFromHistory(
            @AuthenticationPrincipal Utilisateur currentUser,
            @PathVariable Long id) {

        HistoriqueTrajet historique = historiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet historique introuvable"));

        if (!historique.getUtilisateur().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        historiqueRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

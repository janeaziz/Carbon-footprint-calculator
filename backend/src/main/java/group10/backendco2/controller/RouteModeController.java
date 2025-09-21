package group10.backendco2.controller;

import group10.backendco2.dto.RouteModeResponse;
import group10.backendco2.service.GoogleMapService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Contrôleur REST pour gérer les modes de transport.
 *
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Obtenir tous les modes de transport</li>
 *   <li>Créer un nouveau mode de transport (Administrateur uniquement)</li>
 *   <li>Mettre à jour un mode de transport existant (Administrateur uniquement)</li>
 *   <li>Supprimer un mode de transport par ID (Administrateur uniquement)</li>
 *   <li>Rechercher les émissions de CO₂ en fonction de l'origine et de la destination</li>
 * </ul>
 */
@RestController
@RequestMapping("/routes")
public class RouteModeController {
    /**
     * Service métier gérant les opérations liées aux trajets.
     */
    private final GoogleMapService googleMapService;
    
    /**
     * Constructeur pour initialiser le service GoogleMapService.
     *
     * @param googleMapService le service GoogleMapService à injecter
     */
    public RouteModeController(GoogleMapService googleMapService) {
        this.googleMapService = googleMapService;
    }

    /**
     * Récupère tous les modes de transport disponibles dans la base.
     *
     * @return une liste de tous les modes de transport
     */
    @GetMapping("/modes")
    public List<RouteModeResponse> getRoutesByModes(
            @RequestParam String origin,
            @RequestParam String destination) {
        return googleMapService.fetchAllTransportModes(origin, destination);
    }
}

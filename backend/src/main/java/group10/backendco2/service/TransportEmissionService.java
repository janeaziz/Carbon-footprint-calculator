package group10.backendco2.service;

import group10.backendco2.dto.RouteModeResponse;
import group10.backendco2.dto.TransportEmissionDto;
import group10.backendco2.model.CarburantFossile;
import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.SourceElectrique;
import group10.backendco2.model.Trajet;
import group10.backendco2.repository.CarburantFossileRepository;
import group10.backendco2.repository.ModeTransportRepository;
import group10.backendco2.repository.SourceElectriqueRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des émissions de CO2 liées aux transports.
 * <ul>
 *   <li>Calcul des émissions de CO2 pour un trajet donné</li>
 *   <li>Récupération des informations de transport depuis Google Maps</li>
 * </ul>
 */
@Service
public class TransportEmissionService {


  /**
   * Logger pour enregistrer les événements et erreurs.
   */
  private static final Logger logger =
      LoggerFactory.getLogger(TransportEmissionService.class);

  /**
   * Référentiel pour accéder aux données des modes de transport.
   */
  @Autowired private ModeTransportRepository modeTransportRepository;

  /**
   * Référentiel pour accéder aux données des carburants fossiles.
   */
  @Autowired private CarburantFossileRepository carburantFossileRepository;

  /**
   * Référentiel pour accéder aux données des sources électriques.
   */
  @Autowired private SourceElectriqueRepository sourceElectriqueRepository;

  /**
   * Service pour interagir avec l'API Google Maps.
   */
  @Autowired private GoogleMapService googleMapService;


  /**
   * Calcule les émissions de CO2 pour un trajet donné.
   *
   * @param trajet le trajet pour lequel calculer les émissions
   * @return une carte contenant les modes de transport et leurs émissions
   */
  public Map<String, Float> calculateEmissions(Trajet trajet) {
    Map<String, Float> emissions = new HashMap<>();

    Set<ModeTransport> modeSet = trajet.getModesTransport();
    List<ModeTransport> modes = (modeSet != null && !modeSet.isEmpty())
                                    ? new ArrayList<>(modeSet)
                                    : modeTransportRepository.findAll();

    float distanceKm = trajet.getDistance();

    for (ModeTransport mode : modes) {
      Float emissionRate = mode.getSourceEnergie().getEmission(); // g CO₂/km
      Float totalEmission = distanceKm * emissionRate;

      emissions.put(mode.getNom(), totalEmission);
    }

    return emissions;
  }

  /**
   * Récupère le temps estimé pour un trajet donné entre deux points.
   *
   * @param origin le point de départ
   * @param destination le point d'arrivée
   * @return le temps estimé pour le trajet
   */
  public String fetchEstimatedTime(String origin, String destination) {
    return googleMapService.fetchEstimatedTime(origin, destination);
  }

  /**
   * Récupère toutes les informations de transport entre deux points.
   *
   * @param origin le point de départ
   * @param destination le point d'arrivée
   * @return une liste d'objets {@link RouteModeResponse} contenant les
   *     informations de transport
   */
  public List<RouteModeResponse> getAllTransportInfos(String origin,
                                                      String destination) {
    return googleMapService.fetchAllTransportModes(origin, destination);
  }


  /**
   * Extrait le temps en minutes à partir d'une chaîne de texte représentant
   * une durée.
   *
   * @param durationText la chaîne de texte représentant la durée
   * @return le temps en minutes
   */
  private float extractMinutesFromText(String durationText) {
    int totalMinutes = 0;

    try {
      durationText = durationText.toLowerCase();

      if (durationText.contains("day")) {
        String[] parts = durationText.split("day");
        String daysPart = parts[0].replaceAll("[^0-9]", "").trim();
        totalMinutes += Integer.parseInt(daysPart) * 24 * 60;
        if (parts.length > 1)
          durationText = parts[1];
      }

      if (durationText.contains("hour")) {
        String[] parts = durationText.split("hour");
        String hoursPart = parts[0].replaceAll("[^0-9]", "").trim();
        totalMinutes += Integer.parseInt(hoursPart) * 60;
        if (parts.length > 1)
          durationText = parts[1];
      }

      if (durationText.contains("min")) {
        String mins = durationText.replaceAll("[^0-9]", "").trim();
        if (!mins.isEmpty())
          totalMinutes += Integer.parseInt(mins);
      }
    } catch (Exception e) {
      System.err.println("⚠️ Error parsing duration: \"" + durationText + "\"");
      logger.error("Erreur lors de l’appel TransportEmissionService", e);
    }

    return totalMinutes;
  }

  /**
   * Formate le nom du mode de transport pour l'affichage.
   *
   * @param subMode le sous-mode de transport
   * @return le nom formaté du mode de transport
   */
  private String formatLabel(String subMode) {
    return switch (subMode.toLowerCase()) {
      case "bus" -> "Bus";
      case "subway", "metro", "métro" -> "Metro";
      case "tram" -> "Tram";
      case "train", "heavy_rail" -> "TER";
      case "high_speed_train" -> "TGV";
      case "ferry" -> "Ferry";
      default -> subMode;
    };
  }
  

  /**
   * Calcule les émissions de CO2 pour plusieurs modes de transport entre deux
   * points.
   *
   * @param origin le point de départ
   * @param destination le point d'arrivée
   * @return une liste d'objets {@link TransportEmissionDto} contenant les
   *     informations sur les émissions de CO2
   */
  public List<TransportEmissionDto>
  calculateMultiModeEmissions(String origin, String destination) {
    List<RouteModeResponse> transportData =
        googleMapService.fetchAllTransportModes(origin, destination);
    String mapsUrl = "https://www.google.com/maps/dir/" +
                     origin.replace(" ", "+") + "/" +
                     destination.replace(" ", "+");
    List<ModeTransport> allModes = modeTransportRepository.findAll();
    List<TransportEmissionDto> emissions = new ArrayList<>();

    for (int routeIndex = 0; routeIndex < transportData.size(); routeIndex++) {
      RouteModeResponse modeResponse = transportData.get(routeIndex);
      String googleMode = modeResponse.getMode();
      float distance = modeResponse.getDistanceKm();
      float duration = extractMinutesFromText(modeResponse.getEstimatedTime());

      if (!"transit".equals(googleMode)) {
        for (ModeTransport mode : allModes) {
          if (matchesGoogleMode(mode.getNom(), googleMode)) {
            float emissionRate = mode.getSourceEnergie().getEmission();
            float totalEmission = distance * emissionRate;

            String label =
                googleMode.equals("driving")     ? "Fastest driving route"
                : googleMode.equals("walking")   ? "Shortest walking path"
                : googleMode.equals("bicycling") ? "Most efficient bike mode"
                                                 : "-";

            // Crée d’abord le DTO
            TransportEmissionDto dto = new TransportEmissionDto(
                mode.getNom(), totalEmission, distance, duration, null, label);

            dto.setMapsUrl(mapsUrl);

            // Ajoute consommation + unité + prix estimé
            EnergieEtPrix energie = calculerConsoEtPrix(mode, distance);
            dto.setConsommationEnergie(energie.consommation());
            dto.setUnite(energie.unite());
            dto.setPrixEstime(energie.prix());

            emissions.add(dto);
            break; // si un match trouvé, on ne continue pas avec les autres
                   // modes
          }
        }
      }

      else {
        Map<String, Float> subModes = modeResponse.getTransitModes();
        if (subModes == null || subModes.isEmpty()) {
          System.out.println("🚨 No sub-modes found in transit data!");
        } else {

          float totalTransitEmission = 0f;
          float totalTransitDistance = 0f;
          float consommationTotale = 0f;
          float prixTotal = 0f;
          String unite = "-";

          Map<String, Float> distanceBySubMode = new LinkedHashMap<>();

          for (Map.Entry<String, Float> subEntry : subModes.entrySet()) {
            String subMode = subEntry.getKey();
            float subDistance = subEntry.getValue();
            String resolvedSubMode = formatLabel(subMode);
            totalTransitDistance += subDistance;

            distanceBySubMode.merge(subMode, subDistance, Float::sum);

            List<ModeTransport> matchedModes =
                modeTransportRepository.findByNomApprox(resolvedSubMode);

            if (!matchedModes.isEmpty()) {
              ModeTransport mode = matchedModes.get(0);

              float emissionRate = mode.getSourceEnergie().getEmission();
              totalTransitEmission += subDistance * emissionRate;

              EnergieEtPrix energie = calculerConsoEtPrix(mode, subDistance);
              consommationTotale += energie.consommation();
              prixTotal += energie.prix();
              unite = energie.unite();
            }
          }

          StringBuilder labelBuilder = new StringBuilder();
          for (Map.Entry<String, Float> entry : distanceBySubMode.entrySet()) {
            if (labelBuilder.length() > 0) {
              labelBuilder.append(" + ");
            }
            labelBuilder.append(formatLabel(entry.getKey()))
                .append(" (")
                .append(String.format("%.2f km", entry.getValue()))
                .append(")");
          }

          TransportEmissionDto dto = new TransportEmissionDto(
              "Transport en commun", totalTransitEmission, totalTransitDistance,
              duration, labelBuilder.toString(),
              modeResponse.getTransitStepLabelsVerbose() != null
                  ? modeResponse.getTransitStepLabelsVerbose()
                        .stream()
                        .filter(step -> !step.toLowerCase().contains("walk"))
                        .map(step
                             -> step.replace("SUBWAY", "Métro")
                                    .replace("HIGH_SPEED_TRAIN", " ")
                                    .replace("HEAVY_RAIL", " ")
                                    .replace("BUS", "Bus")
                                    .replace("_", " ")
                                    .replace("TRAM", "Tram")
                                    .replace("INTERCITES", "Intercités")
                                    .replaceAll("\\s+", " ")
                                    .trim())
                        .map(step
                             -> step.substring(0, 1).toUpperCase() +
                                    step.substring(1))
                        .distinct()
                        .collect(Collectors.joining(" + "))
                  : "-",
              mapsUrl, labelBuilder.toString());

          dto.setConsommationEnergie(consommationTotale);
          dto.setUnite(unite);
          dto.setPrixEstime(prixTotal);

          emissions.add(dto);
        }
      }
  }

  return filterMostEfficientModes(emissions);
}

/**
 * Supprime les parenthèses et leur contenu d'une chaîne
 * de caractères, en s'assurant
 * que la chaîne résultante ne dépasse pas 100 caractères.
 *  
 * @param input la chaîne d'entrée
 * @return la chaîne sans parenthèses
 */

private String removeParenthesesSafely(String input) {
  int start = input.indexOf('(');
  int end = input.indexOf(')', start);
  if (start != -1 && end != -1 && end > start && (end - start) <= 100) {
    return (input.substring(0, start) + input.substring(end + 1)).trim();
  }
  return input;
}



/**
 * Filtre les modes de transport pour ne garder que les plus efficaces
 * en termes d'émissions de CO2.
 *
 * @param all la liste de tous les modes de transport
 * @return la liste filtrée des modes de transport les plus efficaces
 */
private List<TransportEmissionDto>
filterMostEfficientModes(List<TransportEmissionDto> all) {
  Map<String, TransportEmissionDto> bestModes = new LinkedHashMap<>();

  for (TransportEmissionDto dto : all) {
    String mode = dto.getMode();
    String subMode = dto.getSubMode();

    boolean isTransit = "Transport en commun".equalsIgnoreCase(mode);
    boolean isDriving = mode.toLowerCase().contains("voiture");
    boolean isBicycle = mode.toLowerCase().contains("vélo");
    boolean isWalking = mode.toLowerCase().contains("marche") ||
                        mode.toLowerCase().contains("walking");

    if (isTransit) {
      String cleanedSubMode = dto.getSubMode() != null
                                  ? removeParenthesesSafely(dto.getSubMode())
                                  : "";

      String cleanedLabel =
          dto.getLabel() != null
              ? dto.getLabel().toLowerCase().replaceAll("\\s+", "")
              : "";

      String uniqueKey = cleanedSubMode + "|" + cleanedLabel;

      bestModes.merge(uniqueKey, dto, (existing, incoming) -> {
        boolean incomingBetter =
            incoming.getCo2() < existing.getCo2() &&
            incoming.getDurationMinutes() < existing.getDurationMinutes() &&
            incoming.getDistanceKm() < existing.getDistanceKm();

        return incomingBetter ? incoming : existing;
      });

    } else if (isDriving || isWalking) {
      bestModes.merge(mode, dto,
                      (existing, incoming)
                          -> incoming.getCo2() < existing.getCo2() ? incoming
                                                                   : existing);
    } else if (isBicycle) {
      String key = mode + "-" + (subMode != null ? subMode : "");
      bestModes.merge(key, dto,
                      (existing, incoming)
                          -> incoming.getCo2() < existing.getCo2() ? incoming
                                                                   : existing);
    } else {
      bestModes.put(UUID.randomUUID().toString(), dto);
    }
  }

  return new ArrayList<>(bestModes.values());
}


/**
 * Vérifie si le nom du mode de transport correspond à un mode Google Maps.
 *
 * @param modeName le nom du mode de transport
 * @param googleMode le mode Google Maps
 * @return true si le nom du mode de transport correspond au mode Google Maps,
 *     false sinon
 */
private boolean matchesGoogleMode(String modeName, String googleMode) {
  String normalizedModeName = modeName.toLowerCase(Locale.ROOT).trim();
  String normalizedGoogleMode = googleMode.toLowerCase(Locale.ROOT).trim();

  Map<String, List<String>> googleToDbMap =
      Map.of("driving", List.of("voiture", "thermique", "taxi", "auto"),
             "walking", List.of("marche", "piéton", "à pied"), "bicycling",
             List.of("vélo", "bicyclette", "bike"), "transit",
             List.of("tram", "métro", "rer", "transilien", "intercité", "ter",
                     "tgv", "train", "bus", "ferry"));

  return googleToDbMap.getOrDefault(normalizedGoogleMode, List.of())
      .stream()
      .anyMatch(keyword -> normalizedModeName.contains(keyword));
}



/**
 * Vérifie si le nom du mode de transport correspond à un sous-mode de
 * transport.
 *
 * @param modeName le nom du mode de transport
 * @param subMode le sous-mode de transport
 * @return true si le nom du mode de transport correspond au sous-mode,
 *     false sinon
 */
private boolean matchesTransitSubMode(String modeName, String subMode) {
  if (modeName == null || subMode == null)
    return false;

  String normalizedModeName = modeName.toLowerCase(Locale.ROOT).trim();
  String normalizedSubMode = subMode.toLowerCase(Locale.ROOT).trim();

  Map<String, String> subModeToDbMode =
      Map.of("subway", "subway", "bus", "bus", "tram",
             "bus", // fallback until "Tram" is added to DB
             "ferry", "ferry", "train", "train");

  String expectedDbMode =
      subModeToDbMode.getOrDefault(normalizedSubMode, normalizedSubMode);
  return normalizedModeName.contains(expectedDbMode);
}
/**
 * Calcule la consommation d'énergie et le prix estimé pour un mode de
 * transport donné.
 *
 * @param mode le mode de transport
 * @param distanceKm la distance en kilomètres
 * @return un objet {@link EnergieEtPrix} contenant la consommation, l'unité et
 *     le prix
 */
public record EnergieEtPrix(float consommation, String unite, float prix) {}


/**
 * Calcule la consommation d'énergie et le prix estimé pour un mode de
 * transport donné.
 *
 * @param mode le mode de transport
 * @param distanceKm la distance en kilomètres
 * @return un objet {@link EnergieEtPrix} contenant la consommation, l'unité et
 *     le prix
 */
public EnergieEtPrix calculerConsoEtPrix(ModeTransport mode, float distanceKm) {
  float consommationTotale =
      (mode.getConsommationMoyenne() / 100f) * distanceKm;
  String unite = "-";
  float prix = 0f;

  // Calcul basé sur tarif public au km (s'il est défini)
  Float tarifPublic = mode.getTarifPublicParKm();

  if (tarifPublic != null && tarifPublic > 0f) {
    prix = distanceKm * tarifPublic;
    unite = "kWh";
    return new EnergieEtPrix(consommationTotale, unite, prix);
  }

  Long sourceId = mode.getSourceEnergie().getId();
  List<CarburantFossile> fossiles =
      carburantFossileRepository.findAllBySourceEnergieId(sourceId);

  if (!fossiles.isEmpty()) {
    unite = "L";
    prix = consommationTotale * fossiles.get(0).getPrix();
  } else {
    // Électricité : recherche dynamique
    Optional<SourceElectrique> sourceElec =
        sourceElectriqueRepository.findBySourceEnergieId(sourceId);

    unite = "kWh";
    if (sourceElec.isPresent()) {
      float prixKWH = sourceElec.get().getPrixKWH();
      prix = consommationTotale * prixKWH;
    } else {
      prix = consommationTotale * 0.20f;
    }
  }

  return new EnergieEtPrix(consommationTotale, unite, prix);
}
}

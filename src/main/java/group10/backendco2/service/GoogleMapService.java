package group10.backendco2.service;

import group10.backendco2.dto.RouteModeResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service permettant d’interagir avec l’API Google Maps Directions
 * pour obtenir des données sur les itinéraires, distances, durées,
 * et modes de transport.
 */

@Service
public class GoogleMapService {
  /**
   * Logger pour le service GoogleMapService
   */
  private static final Logger logger =
      LoggerFactory.getLogger(GoogleMapService.class);

  /**
   * Modes de transport pris en charge par l’API Google Maps
   */
  private static final String MODE_DRIVING = "driving";
  private static final String MODE_WALKING = "walking";
  private static final String MODE_BICYCLING = "bicycling";
  private static final String MODE_TRANSIT = "transit";

  /**
   * Message d’erreur en cas de problème avec l’API Google Maps
   */
  private static final String GOOGLE_MAPS_ERROR = "Erreur lors de l’appel Google Maps API";

  /**
   * Clé API Google Maps
   */
  @Value("${google.api.key}") private String apiKey;
  /**
   * Modes de transport pris en charge par l’API Google Maps
   */
  private final String[] MODES = {MODE_DRIVING, MODE_WALKING, MODE_BICYCLING,
                                  MODE_TRANSIT};

  /**
   * Récupère la distance (en km) entre deux lieux en mode "driving" via Google
   * Maps.
   *
   * @param origin point de départ
   * @param destination point d’arrivée
   * @return la distance en kilomètres ou -1 en cas d’échec
   */
  public float fetchDistanceKm(String origin, String destination) {
    try {
      String url = buildUrl(origin, destination, MODE_DRIVING);
      String response = executeCurlCommand(url);
      if (response == null)
        return -1f;
      JSONObject json = new JSONObject(response);

      JSONArray routes = json.getJSONArray("routes");
      if (routes.isEmpty())
        return -1;

      JSONObject leg =
          routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
      int distanceMeters = leg.getJSONObject("distance").getInt("value");

      return distanceMeters / 1000f;
    } catch (Exception e) {
      logger.error(GOOGLE_MAPS_ERROR, e);
      return -1f;
    }
  }

  /**
   * Récupère le temps estimé (sous forme de texte) entre deux lieux en mode
   * "driving" via Google Maps.
   *
   * @param origin point de départ
   * @param destination point d’arrivée
   * @return le temps estimé sous forme de texte ou un message d’erreur
   */
  public String fetchEstimatedTime(String origin, String destination) {
    try {
      String url = buildUrl(origin, destination, MODE_DRIVING);
      String response = executeCurlCommand(url);
      if (response == null)
        return "Temps estimé inconnu";
      JSONObject json = new JSONObject(response);

      JSONArray routes = json.getJSONArray("routes");
      if (routes.isEmpty())
        return "Temps estimé inconnu";

      JSONObject leg =
          routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
      String durationText = leg.getJSONObject("duration").getString("text");

      return "Temps estimé: " + durationText;
    } catch (Exception e) {
      logger.error(GOOGLE_MAPS_ERROR, e);
      return "Erreur lors de la récupération du temps estimé";
    }
  }
  /**
   * Récupère tous les modes de transport entre deux lieux via Google Maps.
   *
   * @param origin point de départ
   * @param destination point d’arrivée
   * @return une liste d’objets RouteModeResponse contenant les informations
   *         sur les itinéraires et les modes de transport
   */
  public List<RouteModeResponse> fetchAllTransportModes(String origin,
                                                        String destination) {
    List<RouteModeResponse> results = new ArrayList<>();

    for (String mode : MODES) {
      try {
        String url = buildUrl(origin, destination, mode);
        String response = executeCurlCommand(url);
        if (response == null)
          continue;

        JSONObject json = new JSONObject(response);
        JSONArray routes = json.optJSONArray("routes");
        if (routes == null || routes.isEmpty())
          continue;

        for (int r = 0; r < routes.length(); r++) {
          JSONObject leg =
              routes.getJSONObject(r).getJSONArray("legs").getJSONObject(0);
          int distanceMeters = leg.getJSONObject("distance").getInt("value");
          String durationText = leg.getJSONObject("duration").getString("text");

          RouteModeResponse modeResponse = new RouteModeResponse();
          modeResponse.setMode(mode);
          modeResponse.setDistanceKm(distanceMeters / 1000f);
          modeResponse.setEstimatedTime(durationText);

          if (mode.equals("transit")) {
            JSONArray steps = leg.getJSONArray("steps");
            Map<String, Float> transitModes = analyzeTransitSteps(steps);
            modeResponse.setTransitModes(transitModes);

            List<String> stepLabels = new ArrayList<>();
            List<String> verboseLabels = new ArrayList<>();
            for (int i = 0; i < steps.length(); i++) {
              JSONObject step = steps.getJSONObject(i);
              String travelMode = step.getString("travel_mode");
              if (travelMode.equalsIgnoreCase("TRANSIT")) {
                JSONObject transitDetails =
                    step.getJSONObject("transit_details");
                JSONObject line = transitDetails.getJSONObject("line");
                String vehicleType = line.getJSONObject("vehicle")
                                         .getString("type")
                                         .toUpperCase();
                String lineName =
                    line.optString("short_name", line.optString("name", ""));
                String from = transitDetails.getJSONObject("departure_stop")
                                  .getString("name");
                String to = transitDetails.getJSONObject("arrival_stop")
                                .getString("name");
                String label = vehicleType + " " + lineName + " (" + from +
                               " → " + to + ")";
                stepLabels.add(vehicleType);
                verboseLabels.add(label);
              } else {
                stepLabels.add("WALK");
                verboseLabels.add("WALK");
              }
            }
            modeResponse.setTransitStepLabels(String.join(" + ", stepLabels));
            modeResponse.setTransitStepLabelsVerbose(verboseLabels);
          }

          results.add(modeResponse);
        }

      } catch (Exception e) {
        logger.error(GOOGLE_MAPS_ERROR, e);
      }
    }

    return results;
  }
  /**
   * Construit l’URL pour l’appel à l’API Google Maps Directions.
   *
   * @param origin point de départ
   * @param destination point d’arrivée
   * @param mode mode de transport
   * @return l’URL construite
   */
  private String buildUrl(String origin, String destination, String mode) {
    return String.format(
        "https://maps.googleapis.com/maps/api/directions/"
            + "json?origin=%s&destination=%s&mode=%s&alternatives=true&key=%s",
        origin.replace(" ", "+"), destination.replace(" ", "+"), mode, apiKey);
  }
  /**
   * Analyse les étapes de transit pour extraire les types de véhicules et
   * leurs distances.
   *
   * @param steps tableau d’étapes
   * @return une carte contenant les types de véhicules et leurs distances
   */
  private Map<String, Float> analyzeTransitSteps(JSONArray steps) {
    Map<String, Float> subModeDistances = new HashMap<>();

    for (int i = 0; i < steps.length(); i++) {
      JSONObject step = steps.getJSONObject(i);
      if (step.getString("travel_mode").equalsIgnoreCase("TRANSIT")) {
        JSONObject transitDetails = step.getJSONObject("transit_details");
        String vehicleType = transitDetails.getJSONObject("line")
                                 .getJSONObject("vehicle")
                                 .getString("type")
                                 .toUpperCase();

        int stepDistance = step.getJSONObject("distance").getInt("value");
        float stepDistanceKm = stepDistance / 1000f;

        subModeDistances.put(vehicleType,
                             subModeDistances.getOrDefault(vehicleType, 0f) +
                                 stepDistanceKm);
      }
    }

    return subModeDistances;
  }
  /**
   * Exécute une commande curl pour appeler l’API Google Maps Directions.
   *
   * @param url l’URL à appeler
   * @return la réponse de l’API ou null en cas d’échec
   */
  public String executeCurlCommand(String url) {
    try {
      ProcessBuilder pb =
          new ProcessBuilder("curl", "-s", "--compressed", "--max-time", "15",
                             "-x", "http://proxy.univ-lyon1.fr:3128", url);

      pb.redirectErrorStream(true);
      Process process = pb.start();

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }

      boolean finished =
          process.waitFor(20, java.util.concurrent.TimeUnit.SECONDS);
      if (!finished) {
        process.destroy();
        System.err.println("❌ Curl timed out and was killed");
        return null;
      }

      int exitCode = process.exitValue();
      if (exitCode == 0) {
        return output.toString();
      } else {
        System.err.println("❌ Curl failed with exit code " + exitCode);
        return null;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error(GOOGLE_MAPS_ERROR, e);
      return null;
    } catch (Exception e) {
      logger.error(GOOGLE_MAPS_ERROR, e);
      return null;
    }
  }
}
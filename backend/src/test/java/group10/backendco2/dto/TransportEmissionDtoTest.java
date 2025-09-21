package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link TransportEmissionDto}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link TransportEmissionDto}
 * pour s'assurer qu'elles fonctionnent correctement.
 */
class TransportEmissionDtoTest {

  /**
   * Vérifie que le constructeur simple initialise les champs de base
   * correctement et que les champs optionnels sont à {@code null}.
   */
  @Test
  void testSimpleConstructor() {
    TransportEmissionDto dto =
        new TransportEmissionDto("Train", 1540.0f, 462.5f, 210.0f);

    assertEquals("Train", dto.getMode());
    assertEquals(1540.0f, dto.getCo2());
    assertEquals(462.5f, dto.getDistanceKm());
    assertEquals(210.0f, dto.getDurationMinutes());
    assertNull(dto.getSubMode());
    assertNull(dto.getLabel());
    assertNull(dto.getMapsUrl());
    assertEquals("462.50 km", dto.getDistanceLabel());
  }
  /**
   * Vérifie que le constructeur avec sous-mode initialise aussi ce champ,
   * en plus des valeurs de base.
   */
  @Test
  void testConstructorWithSubMode() {
    TransportEmissionDto dto =
        new TransportEmissionDto("Bus", 200.0f, 15.0f, 30.0f, "Express");

    assertEquals("Bus", dto.getMode());
    assertEquals(200.0f, dto.getCo2());
    assertEquals(15.0f, dto.getDistanceKm());
    assertEquals(30.0f, dto.getDurationMinutes());
    assertEquals("Express", dto.getSubMode());
    assertNull(dto.getLabel());
    assertNull(dto.getMapsUrl());
    assertEquals("15.00 km", dto.getDistanceLabel());
  }
  /**
   * Vérifie que le constructeur avec sous-mode et label initialise correctement
   * tous les champs associés.
   */
  @Test
  void testConstructorWithSubModeAndLabel() {
    TransportEmissionDto dto = new TransportEmissionDto(
        "Tram", 100.0f, 5.3f, 12.0f, "Night", "TRAM + BUS");

    assertEquals("Tram", dto.getMode());
    assertEquals(100.0f, dto.getCo2());
    assertEquals(5.3f, dto.getDistanceKm());
    assertEquals(12.0f, dto.getDurationMinutes());
    assertEquals("Night", dto.getSubMode());
    assertEquals("TRAM + BUS", dto.getLabel());
    assertNull(dto.getMapsUrl());
    assertEquals("5.30 km", dto.getDistanceLabel());
  }
  /**
   * Vérifie que le constructeur complet initialise tous les champs,
   * y compris {@code mapsUrl} et {@code distanceLabel}.
   */
  @Test
  void testFullConstructor() {
    TransportEmissionDto dto =
        new TransportEmissionDto("Car", 300.0f, 50.0f, 60.0f, "Electric", "CAR",
                                 "http://maps", "CAR (50.00 km)");

    assertEquals("Car", dto.getMode());
    assertEquals(300.0f, dto.getCo2());
    assertEquals(50.0f, dto.getDistanceKm());
    assertEquals(60.0f, dto.getDurationMinutes());
    assertEquals("Electric", dto.getSubMode());
    assertEquals("CAR", dto.getLabel());
    assertEquals("http://maps", dto.getMapsUrl());
    assertEquals("CAR (50.00 km)", dto.getDistanceLabel());
  }
  /**
   * Vérifie que tous les setters définissent correctement les champs
   * et que les getters retournent les bonnes valeurs.
   */
  @Test
  void testSettersAndGetters() {
    TransportEmissionDto dto = new TransportEmissionDto();
    dto.setMode("Bike");
    dto.setCo2(0.0f);
    dto.setDistanceKm(10.0f);
    dto.setDurationMinutes(40.0f);
    dto.setSubMode("Mountain");
    dto.setLabel("BIKE");
    dto.setMapsUrl("http://maps/bike");
    dto.setDistanceLabel("10.00 km");
    dto.setConsommationEnergie(0.0f);
    dto.setUnite("kWh");
    dto.setPrixEstime(0.0f);

    assertEquals("Bike", dto.getMode());
    assertEquals(0.0f, dto.getCo2());
    assertEquals(10.0f, dto.getDistanceKm());
    assertEquals(40.0f, dto.getDurationMinutes());
    assertEquals("Mountain", dto.getSubMode());
    assertEquals("BIKE", dto.getLabel());
    assertEquals("http://maps/bike", dto.getMapsUrl());
    assertEquals("10.00 km", dto.getDistanceLabel());
    assertEquals(0.0f, dto.getConsommationEnergie());
    assertEquals("kWh", dto.getUnite());
    assertEquals(0.0f, dto.getPrixEstime());
  }
  /**
   * Vérifie que si {@code distanceLabel} est nul, il est automatiquement
   * généré via la méthode {@code formatDistance()}.
   */
  @Test
  void testDistanceLabelFallback() {
    TransportEmissionDto dto = new TransportEmissionDto(
        "Walk", 0.0f, 2.0f, 25.0f, null, null, null, null);
    assertEquals("2.00 km", dto.getDistanceLabel());
  }
}
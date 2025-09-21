package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link SimulationRequest}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link SimulationRequest} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class SimulationRequestTest {

  /** Vérifie le getter et setter pour le champ origine. */

  @Test
  void testOrigineGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setOrigine("Lyon");
    assertEquals("Lyon", req.getOrigine());
  }
  /** Vérifie le getter et setter pour le champ destination. */

  @Test
  void testDestinationGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setDestination("Paris");
    assertEquals("Paris", req.getDestination());
  }
  /** Vérifie le getter et setter pour le champ modeTransport. */
  @Test
  void testModeTransportGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setModeTransport("Voiture Essence");
    assertEquals("Voiture Essence", req.getModeTransport());
  }
  /** Vérifie le getter et setter pour le champ frequency. */
  @Test
  void testFrequencyGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setFrequency("quotidien");
    assertEquals("quotidien", req.getFrequency());
  }
  /** Vérifie le getter et setter pour le champ duration. */
  @Test
  void testDurationGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setDuration(30);
    assertEquals(30, req.getDuration());
  }
  /** Vérifie le getter et setter pour le champ totalEmission. */
  @Test
  void testTotalEmissionGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setTotalEmission(6543.5f);
    assertEquals(6543.5f, req.getTotalEmission());
  }
  /** Vérifie le getter et setter pour le champ utilisateurId. */
  @Test
  void testUtilisateurIdGetterSetter() {
    SimulationRequest req = new SimulationRequest();
    req.setUtilisateurId(1L);
    assertEquals(1L, req.getUtilisateurId());
  }
  /** Vérifie le getter et setter pour tous les champs ensemble. */
  @Test
  void testAllFieldsTogether() {
    SimulationRequest req = new SimulationRequest();
    req.setOrigine("Lyon");
    req.setDestination("Paris");
    req.setModeTransport("Train");
    req.setFrequency("hebdomadaire");
    req.setDuration(15);
    req.setTotalEmission(1234.56f);
    req.setUtilisateurId(42L);

    assertEquals("Lyon", req.getOrigine());
    assertEquals("Paris", req.getDestination());
    assertEquals("Train", req.getModeTransport());
    assertEquals("hebdomadaire", req.getFrequency());
    assertEquals(15, req.getDuration());
    assertEquals(1234.56f, req.getTotalEmission());
    assertEquals(42L, req.getUtilisateurId());
  }
}
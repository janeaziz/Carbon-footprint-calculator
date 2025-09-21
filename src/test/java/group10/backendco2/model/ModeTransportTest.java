package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link ModeTransport}.
 * <p>
 * Vérifie que les constructeurs (sans et avec arguments) et les accesseurs
 * (getters/setters) fonctionnent correctement.
 */
class ModeTransportTest {

  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructorAndSettersAndGetters() {
    ModeTransport mode = new ModeTransport();
    mode.setId(10L);
    mode.setNom("Bus");
    mode.setConsommationMoyenne(20.5f);
    mode.setCapacite(50);

    TypeTransport typeTransport = new TypeTransport();
    mode.setTypeTransport(typeTransport);

    SourceEnergie sourceEnergie = new SourceEnergie();
    mode.setSourceEnergie(sourceEnergie);

    mode.setTarifPublicParKm(0.25f);

    assertEquals(10L, mode.getId());
    assertEquals("Bus", mode.getNom());
    assertEquals(20.5f, mode.getConsommationMoyenne());
    assertEquals(50, mode.getCapacite());
    assertSame(typeTransport, mode.getTypeTransport());
    assertSame(sourceEnergie, mode.getSourceEnergie());
    assertEquals(0.25f, mode.getTarifPublicParKm());
  }

  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code id}, {@code nom}, {@code consommationMoyenne},
   * {@code capacite}, {@code typeTransport}, {@code sourceEnergie} et
   * {@code tarifPublicParKm}.
   */
  @Test
  void testAllArgsConstructor() {
    TypeTransport typeTransport = new TypeTransport();
    SourceEnergie sourceEnergie = new SourceEnergie();

    ModeTransport mode =
        new ModeTransport(1L, "Electric Car", 15.5f, Integer.valueOf(5),
                          typeTransport, sourceEnergie, Float.valueOf(0.15f));

    assertEquals(1L, mode.getId());
    assertEquals("Electric Car", mode.getNom());
    assertEquals(15.5f, mode.getConsommationMoyenne());
    assertEquals(5, mode.getCapacite());
    assertSame(typeTransport, mode.getTypeTransport());
    assertSame(sourceEnergie, mode.getSourceEnergie());
    assertEquals(0.15f, mode.getTarifPublicParKm());
  }

  /**
   * Vérifie que tous les champs de {@link ModeTransport} acceptent les valeurs
   * {@code null} ou par défaut (comme {@code 0f} pour les primitives), et que
   * les getters les retournent correctement.
   */
  @Test
  void testSettersWithNullValues() {
    ModeTransport mode = new ModeTransport();
    mode.setId(null);
    mode.setNom(null);
    mode.setConsommationMoyenne(0f);
    mode.setCapacite(null);
    mode.setTypeTransport(null);
    mode.setSourceEnergie(null);
    mode.setTarifPublicParKm(null);

    assertNull(mode.getId());
    assertNull(mode.getNom());
    assertEquals(0f, mode.getConsommationMoyenne());
    assertNull(mode.getCapacite());
    assertNull(mode.getTypeTransport());
    assertNull(mode.getSourceEnergie());
    assertNull(mode.getTarifPublicParKm());
  }
}
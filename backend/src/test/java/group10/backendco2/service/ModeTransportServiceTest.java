package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import group10.backendco2.model.ModeTransport;
import group10.backendco2.model.SourceEnergie;
import group10.backendco2.model.TypeTransport;
import group10.backendco2.repository.ModeTransportRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la classe {@link ModeTransportService}.
 *
 * Ces tests valident le comportement de la méthode {@code update}
 * lorsqu’un mode de transport est mis à jour ou non trouvé.
 */
class ModeTransportServiceTest {
  /**
   * Repository pour les modes de transport.
   */
  private ModeTransportRepository modeTransportRepository;

  /**
   * Service pour les modes de transport.
   */
  private ModeTransportService modeTransportService;

  /**
   * Prépare le service ModeTransportService avant chaque test.
   *
   * Initialise le repository et injecte une instance fictive pour éviter les
   * appels réels.
   */
  @BeforeEach
  void setUp() {
    modeTransportRepository = mock(ModeTransportRepository.class);
    modeTransportService = new ModeTransportService();
    try {
      var field = ModeTransportService.class.getDeclaredField(
          "modeTransportRepository");
      field.setAccessible(true);
      field.set(modeTransportService, modeTransportRepository);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Vérifie que la méthode {@code update} met à jour correctement un
   * {@link ModeTransport} existant avec les nouvelles valeurs fournies,
   * et retourne le résultat mis à jour.
   */
  @Test
  void update_shouldUpdateAndReturnUpdatedModeTransport_whenIdExists() {
    Long id = 1L;

    TypeTransport existingType = new TypeTransport();
    existingType.setNom("Tram");
    existingType.setDescription("Ancienne description");

    TypeTransport updatedType = new TypeTransport();
    updatedType.setNom("Tram");
    updatedType.setDescription("Transport collectif");

    ModeTransport existing = new ModeTransport();
    existing.setNom("Old Name");
    existing.setConsommationMoyenne(10.0f);
    existing.setCapacite(50);
    existing.setTypeTransport(existingType);
    SourceEnergie diesel = new SourceEnergie();
    diesel.setNom("Diesel");
    diesel.setEmission(2390.0f);
    existing.setSourceEnergie(diesel);

    ModeTransport updated = new ModeTransport();
    updated.setNom("New Name");
    updated.setConsommationMoyenne(8.5f);
    updated.setCapacite(60);
    updated.setTypeTransport(updatedType);
    SourceEnergie electric = new SourceEnergie();
    electric.setNom("Électrique");
    electric.setEmission(0.0f);
    updated.setSourceEnergie(electric);

    when(modeTransportRepository.findById(id))
        .thenReturn(Optional.of(existing));
    when(modeTransportRepository.save(any(ModeTransport.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Optional<ModeTransport> result = modeTransportService.update(id, updated);

    assertTrue(result.isPresent());
    ModeTransport saved = result.get();
    assertEquals("New Name", saved.getNom());
    assertEquals(8.5f, saved.getConsommationMoyenne());
    assertEquals(60, saved.getCapacite());
    assertEquals("Tram", saved.getTypeTransport().getNom());
    assertEquals("Électrique", saved.getSourceEnergie().getNom());
    assertEquals(0.0f, saved.getSourceEnergie().getEmission());

    verify(modeTransportRepository).findById(id);
    verify(modeTransportRepository).save(existing);
  }
  /**
   * Vérifie que la méthode {@code update} retourne un {@link Optional#empty()}
   * lorsque l’identifiant fourni ne correspond à aucun mode de transport
   * existant.
   */
  @Test
  void update_shouldReturnEmpty_whenIdDoesNotExist() {
    Long id = 2L;
    ModeTransport updated = new ModeTransport();

    when(modeTransportRepository.findById(id)).thenReturn(Optional.empty());

    Optional<ModeTransport> result = modeTransportService.update(id, updated);

    assertFalse(result.isPresent());
    verify(modeTransportRepository).findById(id);
    verify(modeTransportRepository, never()).save(any());
  }
}

package group10.backendco2.repository;

import group10.backendco2.model.Trajet;
import group10.backendco2.model.ModeTransport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests d'intégration pour {@link TrajetRepository}.
 *
 * Vérifie le bon fonctionnement de la méthode personnalisée {@code
 * findByIdWithModes(Long)}, notamment : <ul> <li>la récupération d'un trajet
 * avec ses modes de transport associés</li> <li>le comportement lorsqu'aucun
 * résultat n'est trouvé</li>
 * </ul>
 */
@DataJpaTest
class TrajetRepositoryTest {
    /**
     * Repository pour les trajets.
     */
    @Autowired
    private TrajetRepository trajetRepository;
    /**
     * Repository pour les modes de transport.
     */
    @Autowired
    private ModeTransportRepository modeTransportRepository;
    /**
     * Vérifie que {@code findByIdWithModes(Long)} retourne un {@link Trajet} si
     * il est associé à des {@link ModeTransport} enregistrés.
     */
    @Test
    @DisplayName("findByIdWithModes returns Trajet with modesTransport when present")
    void testFindByIdWithModes_found() {
        // Arrange
        Trajet trajet = new Trajet();
        ModeTransport mode = new ModeTransport();
        mode.setNom("Bus");
        mode = modeTransportRepository.save(mode);
        trajet.setModesTransport(new java.util.HashSet<>(Set.of(mode)));
        trajet = trajetRepository.save(trajet);
        // Act
        Optional<Trajet> found = trajetRepository.findByIdWithModes(trajet.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getModesTransport()).isNotNull();
        assertThat(found.get().getModesTransport()).hasSize(1);
        assertThat(found.get().getModesTransport().iterator().next().getNom()).isEqualTo("Bus");
    }
    /**
     * Vérifie que {@code findByIdWithModes(Long)} retourne un {@link Optional}
     * vide si aucun enregistrement n’est associé à l’identifiant donné.
     */
    @Test
    @DisplayName("findByIdWithModes returns empty when Trajet not found")
    void testFindByIdWithModes_notFound() {
        Optional<Trajet> found = trajetRepository.findByIdWithModes(999L);
        assertThat(found).isEmpty();
    }
}
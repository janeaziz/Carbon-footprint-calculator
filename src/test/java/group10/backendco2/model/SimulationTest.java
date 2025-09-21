package group10.backendco2.model;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires de la classe {@link Simulation}.
 * <p>
 * Vérifie que les constructeurs (sans et avec arguments) et les accesseurs
 * (getters/setters) fonctionnent correctement.
 */
class SimulationTest {
    /**
     * Vérifie que le constructeur sans argument initialise un objet vide
     * et que tous les setters et getters fonctionnent correctement.
     */
    @Test
    void testNoArgsConstructorAndSetters() {
        Simulation simulation = new Simulation();
        simulation.setId(1L);
        Date date = new Date();
        simulation.setDateSimulation(date);
        simulation.setOrigine("Lyon");
        simulation.setDestination("Paris");
        simulation.setModeTransport("Train");
        simulation.setFrequency("daily");
        simulation.setDuration(30);
        simulation.setTotalEmission(6543.25f);

        Utilisateur utilisateur = new Utilisateur();
        simulation.setUtilisateur(utilisateur);

        assertEquals(1L, simulation.getId());
        assertEquals(date, simulation.getDateSimulation());
        assertEquals("Lyon", simulation.getOrigine());
        assertEquals("Paris", simulation.getDestination());
        assertEquals("Train", simulation.getModeTransport());
        assertEquals("daily", simulation.getFrequency());
        assertEquals(30, simulation.getDuration());
        assertEquals(6543.25f, simulation.getTotalEmission());
        assertEquals(utilisateur, simulation.getUtilisateur());
    }
    /**
     * Vérifie que le constructeur avec tous les arguments initialise correctement
     * tous les champs {@code id}, {@code dateSimulation}, {@code origine},
     * {@code destination}, {@code modeTransport}, {@code frequency},
     * {@code duration}, {@code totalEmission} et {@code utilisateur}.
     */
    @Test
    void testAllArgsConstructor() {
        Date date = new Date();
        Utilisateur utilisateur = new Utilisateur();
        Simulation simulation = new Simulation(
                2L,
                date,
                "Marseille",
                "Nice",
                "Bus",
                "weekly",
                7,
                123.45f,
                utilisateur
        );

        assertEquals(2L, simulation.getId());
        assertEquals(date, simulation.getDateSimulation());
        assertEquals("Marseille", simulation.getOrigine());
        assertEquals("Nice", simulation.getDestination());
        assertEquals("Bus", simulation.getModeTransport());
        assertEquals("weekly", simulation.getFrequency());
        assertEquals(7, simulation.getDuration());
        assertEquals(123.45f, simulation.getTotalEmission());
        assertEquals(utilisateur, simulation.getUtilisateur());
    }
}
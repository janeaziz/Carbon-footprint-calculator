package group10.backendco2.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link AbstractSimulationData}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link AbstractSimulationData} pour s'assurer qu'elles
 * fonctionnent correctement.
 */

class AbstractSimulationDataTest {

    /**
     * Classe de test pour {@link AbstractSimulationData}.
     * <p>
     * Cette classe étend {@link AbstractSimulationData} pour permettre la création d'une instance
     * concrète à des fins de test.
     */
    static class SimulationDataImpl extends AbstractSimulationData {}


    /**
     * Teste les accesseurs et mutateurs de la classe {@link AbstractSimulationData}.
     * <p>
     * Cette méthode crée une instance de {@link SimulationDataImpl} et teste les accesseurs et
     * mutateurs pour chaque attribut.
     */
    @Test
    void testGettersAndSetters() {
        SimulationDataImpl data = new SimulationDataImpl();

        data.setOrigine("Lyon");
        data.setDestination("Paris");
        data.setModeTransport("Train");
        data.setFrequency("quotidienne");
        data.setDuration(30);
        data.setTotalEmission(6543.25f);

        assertEquals("Lyon", data.getOrigine());
        assertEquals("Paris", data.getDestination());
        assertEquals("Train", data.getModeTransport());
        assertEquals("quotidienne", data.getFrequency());
        assertEquals(30, data.getDuration());
        assertEquals(6543.25f, data.getTotalEmission(), 0.0001f);
    }

    /**
     * Teste les valeurs par défaut de la classe {@link AbstractSimulationData}.
     * <p>
     * Cette méthode crée une instance de {@link SimulationDataImpl} et teste les valeurs par défaut
     * des attributs.
     */
    @Test
    void testDefaultValues() {
        SimulationDataImpl data = new SimulationDataImpl();

        assertNull(data.getOrigine());
        assertNull(data.getDestination());
        assertNull(data.getModeTransport());
        assertNull(data.getFrequency());
        assertEquals(0, data.getDuration());
        assertEquals(0.0f, data.getTotalEmission(), 0.0001f);
    }
}
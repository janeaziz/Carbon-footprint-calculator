package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link HistoriqueTrajet}.
 *
 * Vérifie que les constructeurs (sans et avec arguments) et les accesseurs
 * (getters/setters) fonctionnent correctement.
 */

class HistoriqueTrajetTest {
  /**
   * Vérifie que le constructeur sans argument initialise un objet vide
   * et que tous les setters et getters fonctionnent correctement.
   */
  @Test
  void testNoArgsConstructorAndSetters() {
    HistoriqueTrajet historique = new HistoriqueTrajet();
    historique.setId(10L);
    Date date = new Date();
    historique.setDateRealisation(date);

    Utilisateur utilisateur = mock(Utilisateur.class);
    historique.setUtilisateur(utilisateur);

    Trajet trajet = mock(Trajet.class);
    historique.setTrajet(trajet);

    assertEquals(10L, historique.getId());
    assertEquals(date, historique.getDateRealisation());
    assertEquals(utilisateur, historique.getUtilisateur());
    assertEquals(trajet, historique.getTrajet());
  }
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs {@code id}, {@code dateRealisation}, {@code utilisateur} et
   * {@code trajet}.
   */
  @Test
  void testAllArgsConstructor() {
    Long id = 5L;
    Date date = new Date();
    Utilisateur utilisateur = mock(Utilisateur.class);
    Trajet trajet = mock(Trajet.class);

    HistoriqueTrajet historique =
        new HistoriqueTrajet(id, date, utilisateur, trajet);

    assertEquals(id, historique.getId());
    assertEquals(date, historique.getDateRealisation());
    assertEquals(utilisateur, historique.getUtilisateur());
    assertEquals(trajet, historique.getTrajet());
  }
}
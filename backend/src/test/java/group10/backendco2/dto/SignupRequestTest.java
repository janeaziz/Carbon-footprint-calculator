
package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link SignupRequest}.
 *
 * Vérifie la bonne affectation et récupération des champs publics :
 * <ul>
 *   <li>nom</li>
 *   <li>email</li>
 *   <li>motDePasse</li>
 * </ul>
 */

class SignupRequestTest {
  /**
   * Vérifie que les champs {@code nom}, {@code email} et {@code motDePasse}
   * peuvent être correctement affectés et récupérés.
   */
  @Test
  void testSignupRequestFieldsAssignment() {
    SignupRequest request = new SignupRequest();
    request.nom = "Jean Dupont";
    request.email = "jeandupont@example.com";
    request.motDePasse = "motDePasseSecurise123";

    assertEquals("Jean Dupont", request.nom);
    assertEquals("jeandupont@example.com", request.email);
    assertEquals("motDePasseSecurise123", request.motDePasse);
  }
  /**
   * Vérifie que les champs {@code nom}, {@code email} et {@code motDePasse}
   * sont {@code null} par défaut à l'instanciation.
   */
  @Test
  void testSignupRequestDefaultValues() {
    SignupRequest request = new SignupRequest();
    assertNull(request.nom);
    assertNull(request.email);
    assertNull(request.motDePasse);
  }
}
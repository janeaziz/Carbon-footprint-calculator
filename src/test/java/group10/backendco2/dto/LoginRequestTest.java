package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la classe {@link LoginRequest}.
 * <p>
 * Cette classe teste les méthodes de la classe {@link LoginRequest} pour
 * s'assurer qu'elles fonctionnent correctement.
 */
class LoginRequestTest {
  /**
   * Vérifie que le getter {@code getEmail()} retourne bien l'adresse email
   * définie par {@code setEmail()}.
   */
  @Test
  void testGetEmail_ReturnsSetEmail() {
    LoginRequest loginRequest = new LoginRequest();
    String testEmail = "user@example.com";
    loginRequest.setEmail(testEmail);
    assertEquals(testEmail, loginRequest.getEmail());
  }
  /**
   * Vérifie que l’email est {@code null} par défaut à l’instanciation d’un
   * {@link LoginRequest}.
   */
  @Test
  void testGetEmail_DefaultIsNull() {
    LoginRequest loginRequest = new LoginRequest();
    assertNull(loginRequest.getEmail());
  }
}
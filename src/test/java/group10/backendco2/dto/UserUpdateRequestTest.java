package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
/**
 * Tests unitaires pour la classe {@link UserUpdateRequest}.
 *
 * Vérifie le comportement des getters et setters pour :
 * <ul>
 *   <li>{@code name}</li>
 *   <li>{@code password}</li>
 * </ul>
 * Y compris la gestion des valeurs {@code null}.
 */

class UserUpdateRequestTest {
  /**
   * Vérifie que le champ {@code name} est initialement {@code null},
   * puis correctement défini et récupéré après affectation.
   */
  @Test
  void testSetAndGetName() {
    UserUpdateRequest request = new UserUpdateRequest();
    assertNull(request.getName());
    String name = "Alice Dupont";
    request.setName(name);
    assertEquals(name, request.getName());
  }
  /**
   * Vérifie que le champ {@code password} est initialement {@code null},
   * puis correctement défini et récupéré après affectation.
   */
  @Test
  void testSetAndGetPassword() {
    UserUpdateRequest request = new UserUpdateRequest();
    assertNull(request.getPassword());
    String password = "MotDePasse123!";
    request.setPassword(password);
    assertEquals(password, request.getPassword());
  }
  /**
   * Vérifie que le champ {@code name} peut être réinitialisé à {@code null}
   * après avoir été défini.
   */
  @Test
  void testSetNameToNull() {
    UserUpdateRequest request = new UserUpdateRequest();
    request.setName("Bob");
    request.setName(null);
    assertNull(request.getName());
  }
  /**
   * Vérifie que le champ {@code password} peut être réinitialisé à {@code null}
   * après avoir été défini.
   */
  @Test
  void testSetPasswordToNull() {
    UserUpdateRequest request = new UserUpdateRequest();
    request.setPassword("secret");
    request.setPassword(null);
    assertNull(request.getPassword());
  }
}
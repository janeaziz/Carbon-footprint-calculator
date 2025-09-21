package group10.backendco2.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la classe {@link UserResponseDto}.
 *
 * Vérifie le bon fonctionnement des constructeurs, getters, setters,
 * ainsi que la gestion des valeurs nulles.
 */

class UserResponseDtoTest {
  /**
   * Vérifie que le constructeur avec tous les arguments initialise correctement
   * tous les champs, et utilise la réflexion pour valider les valeurs internes
   * privées si nécessaire.
   */
  @Test
  void testConstructorAndGetters() {
    Long id = 1L;
    String nom = "John Doe";
    String email = "john.doe@example.com";
    String role = "USER";
    LocalDate dateInscription = LocalDate.of(2024, 6, 1);

    UserResponseDto dto =
        new UserResponseDto(id, nom, email, role, dateInscription);

    try {
      var idField = UserResponseDto.class.getDeclaredField("id");
      idField.setAccessible(true);
      assertEquals(id, idField.get(dto));

      var nomField = UserResponseDto.class.getDeclaredField("nom");
      nomField.setAccessible(true);
      assertEquals(nom, nomField.get(dto));

      var emailField = UserResponseDto.class.getDeclaredField("email");
      emailField.setAccessible(true);
      assertEquals(email, emailField.get(dto));
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }

    assertEquals(role, dto.getRole());
    assertEquals(dateInscription, dto.getDateInscription());
  }
  /**
   * Vérifie le bon fonctionnement des setters pour {@code role} et {@code
   * dateInscription}.
   */
  @Test
  void testSetters() {
    UserResponseDto dto = new UserResponseDto(
        2L, "Jane Doe", "jane@example.com", "ADMIN", LocalDate.now());

    dto.setRole("SUPERADMIN");
    assertEquals("SUPERADMIN", dto.getRole());

    LocalDate newDate = LocalDate.of(2023, 1, 1);
    dto.setDateInscription(newDate);
    assertEquals(newDate, dto.getDateInscription());
  }
  /**
   * Vérifie tous les getters et setters en les combinant sur un même objet,
   * incluant modification après instanciation.
   */
  @Test
  void testGettersAndSetters() {
    UserResponseDto dto = new UserResponseDto(
        10L, "Alice", "alice@example.com", "USER", LocalDate.of(2022, 5, 10));

    // Test id
    assertEquals(10L, dto.getId());
    dto.setId(20L);
    assertEquals(20L, dto.getId());

    // Test nom
    assertEquals("Alice", dto.getNom());
    dto.setNom("Bob");
    assertEquals("Bob", dto.getNom());

    // Test email
    assertEquals("alice@example.com", dto.getEmail());
    dto.setEmail("bob@example.com");
    assertEquals("bob@example.com", dto.getEmail());

    // Test role
    assertEquals("USER", dto.getRole());
    dto.setRole("ADMIN");
    assertEquals("ADMIN", dto.getRole());

    // Test dateInscription
    assertEquals(LocalDate.of(2022, 5, 10), dto.getDateInscription());
    LocalDate newDate = LocalDate.of(2023, 12, 31);
    dto.setDateInscription(newDate);
    assertEquals(newDate, dto.getDateInscription());
  }
  /**
   * Vérifie que le DTO accepte des valeurs nulles à l’instanciation
   * et que les setters permettent d’ajouter les valeurs ensuite.
   */
  @Test
  void testNullValues() {
    UserResponseDto dto = new UserResponseDto(null, null, null, null, null);

    assertNull(dto.getId());
    assertNull(dto.getNom());
    assertNull(dto.getEmail());
    assertNull(dto.getRole());
    assertNull(dto.getDateInscription());

    dto.setId(5L);
    dto.setNom("Test");
    dto.setEmail("test@test.com");
    dto.setRole("TEST");
    dto.setDateInscription(LocalDate.of(2020, 1, 1));

    assertEquals(5L, dto.getId());
    assertEquals("Test", dto.getNom());
    assertEquals("test@test.com", dto.getEmail());
    assertEquals("TEST", dto.getRole());
    assertEquals(LocalDate.of(2020, 1, 1), dto.getDateInscription());
  }
}
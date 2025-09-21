package group10.backendco2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;


/**
 * Tests unitaires pour la classe {@link Utilisateur}.
 * <p>
 * Vérifie le comportement des getters et setters pour :
 * <ul>
 *   <li>{@code id}</li>
 *   <li>{@code nom}</li>
 *   <li>{@code email}</li>
 *   <li>{@code motDePasse}</li>
 *   <li>{@code dateInscription}</li>
 *   <li>{@code role}</li>
 * </ul>
 */

class UtilisateurTest {
  /**
   * Vérifie que l’identifiant {@code id} de {@link Utilisateur} peut être
   * défini et récupéré correctement.
   */
  @Test
  void testIdGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setId(42L);
    assertEquals(42L, utilisateur.getId());
  }
  /**
   * Vérifie que le champ {@code email} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testNomGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setNom("Alice Dupont");
    assertEquals("Alice Dupont", utilisateur.getNom());
  }
  /**
   * Vérifie que le champ {@code email} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testEmailGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setEmail("alice@example.com");
    assertEquals("alice@example.com", utilisateur.getEmail());
  }
  /**
   * Vérifie que le champ {@code motDePasse} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testMotDePasseGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setMotDePasse("encryptedPassword123");
    assertEquals("encryptedPassword123", utilisateur.getMotDePasse());
  }
  /**
   * Vérifie que le champ {@code dateInscription} peut être défini et récupéré
   * correctement.
   */
  @Test
  void testDateInscriptionGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    LocalDate date = LocalDate.of(2024, 6, 1);
    utilisateur.setDateInscription(date);
    assertEquals(date, utilisateur.getDateInscription());
  }
  /**
   * Vérifie que la valeur par défaut du champ {@code role} est {@link
   * Utilisateur.Role#Visiteur}.
   */
  @Test
  void testRoleDefaultValue() {
    Utilisateur utilisateur = new Utilisateur();
    assertEquals(Utilisateur.Role.Visiteur, utilisateur.getRole());
  }
  /**
   * Vérifie que le champ {@code role} peut être défini avec différentes valeurs
   * de l’énumération
   * {@link Utilisateur.Role} et que les modifications sont correctement prises
   * en compte.
   */
  @Test
  void testRoleGetterAndSetter() {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setRole(Utilisateur.Role.Admin);
    assertEquals(Utilisateur.Role.Admin, utilisateur.getRole());

    utilisateur.setRole(Utilisateur.Role.Normal);
    assertEquals(Utilisateur.Role.Normal, utilisateur.getRole());
  }
}

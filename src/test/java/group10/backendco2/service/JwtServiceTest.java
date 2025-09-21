package group10.backendco2.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Tests unitaires pour la classe {@link JwtService}.
 *
 * Vérifie le comportement de la génération et la validation des tokens JWT :
 * <ul>
 *   <li>Validité d’un token bien formé et non expiré</li>
 *   <li>Détection d’un token invalide</li>
 *   <li>Détection d’un token expiré</li>
 * </ul>
 */
class JwtServiceTest {
  /**
   * Service de gestion des tokens JWT.
   */
  private JwtService jwtService;
  /**
   * Clé secrète utilisée pour signer les tokens JWT.
   */
  private final String secret = "mysecretkeymysecretkeymysecretkey12";
  /**
   * Durée d'expiration des tokens JWT en millisecondes.
   */
  private final long expiration = 10000L; // 10 secondes
  /**
   * Prépare le service JwtService avant chaque test.
   *
   * Initialise le service et injecte la clé secrète et la durée d'expiration
   * pour éviter les appels réels.
   */
  @BeforeEach
  void setUp() {
    jwtService = new JwtService();
    ReflectionTestUtils.setField(jwtService, "jwtSecret", secret);
    ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", expiration);
  }
  /**
   * Vérifie que {@code validateToken} retourne {@code true}
   * pour un token valide généré par {@code generateToken}.
   */
  @Test
  void testValidateToken_withValidToken_returnsTrue() {
    String token = jwtService.generateToken("test@example.com", "USER");
    assertTrue(jwtService.validateToken(token));
  }
  /**
   * Vérifie que {@code validateToken} retourne {@code false}
   * pour une chaîne qui n'est pas un token JWT valide.
   */
  @Test
  void testValidateToken_withInvalidToken_returnsFalse() {
    String invalidToken = "invalid.token.value";
    assertFalse(jwtService.validateToken(invalidToken));
  }
  /**
   * Vérifie que {@code validateToken} retourne {@code false}
   * lorsqu’un token JWT est expiré.
   */
  @Test
  void testValidateToken_withExpiredToken_returnsFalse() {
    java.security.Key signingKey = jwtService.getSigningKey();
    String expiredToken =
        Jwts.builder()
            .setSubject("test@example.com")
            .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
            .setExpiration(new Date(System.currentTimeMillis() - 10000))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    assertFalse(jwtService.validateToken(expiredToken));
  }
}
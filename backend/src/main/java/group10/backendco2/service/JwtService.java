package group10.backendco2.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des tokens JWT :
 * <ul>
 *   <li>Génération de tokens avec rôles</li>
 *   <li>Extraction des informations utilisateur depuis un token</li>
 *   <li>Validation de la signature et de la durée de validité d’un token</li>
 * </ul>
 */

@Service
public class JwtService {

  /**
   * Clé secrète utilisée pour signer les tokens JWT (injectée depuis
   * application.properties).
   */
  @Value("${jwt.secret}") private String jwtSecret;

  /** Durée de validité des tokens JWT, en millisecondes. */
  @Value("${jwt.expiration}") private long jwtExpirationMs;

  /**
   * Récupère la clé secrète utilisée pour signer les tokens JWT.
   *
   * @return la clé secrète
   */
  public Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  /**
   * Génère un token JWT pour un utilisateur donné.
   *
   * @param email l'email de l'utilisateur
   * @param role le rôle de l'utilisateur
   * @return le token JWT généré
   */
  public String generateToken(String email, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);

    return Jwts.builder()
        .setSubject(email)
        .addClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Génère un token JWT pour un utilisateur donné.
   *
   * @param email l'email de l'utilisateur
   * @return le token JWT généré
   */
  public String getEmailFromToken(String token) {
    return parseToken(token).getBody().getSubject();
  }

  /**
   * Extrait le rôle de l’utilisateur depuis les claims du token JWT.
   *
   * @param token le token JWT
   * @return le rôle de l’utilisateur (ex : "User", "Admin")
   */

  public String getRoleFromToken(String token) {
    return (String)parseToken(token).getBody().get("role");
  }

  /**
   * Vérifie la validité d’un token JWT (signature et expiration).
   *
   * @param token le token JWT à valider
   * @return {@code true} si le token est valide, {@code false} sinon
   */

  public boolean validateToken(String token) {
    try {
      parseToken(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Parse et valide le token JWT en utilisant la clé de signature.
   *
   * @param token le token JWT à parser
   * @return un objet {@link Jws} contenant les claims extraits du token
   * @throws JwtException si le token est invalide ou expiré
   */

  private Jws<Claims> parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token);
  }
}

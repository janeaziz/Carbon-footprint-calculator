package group10.backendco2.config;

import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.UtilisateurRepository;
import group10.backendco2.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtre d'authentification JWT exécuté une seule fois par requête HTTP.
 *
 * Ce filtre intercepte les requêtes entrantes pour :
 * <ul>
 *   <li>Extraire et valider le token JWT dans l'en-tête Authorization</li>
 *   <li>Récupérer l'utilisateur associé au token</li>
 *   <li>Configurer le contexte de sécurité de Spring si le token est
 * valide</li>
 * </ul>
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * Service de gestion des tokens JWT (validation, extraction d'information).
   */
  @Autowired private JwtService jwtService;

  /**
   * Référentiel d'accès aux utilisateurs, utilisé pour récupérer l'utilisateur
   * authentifié.
   */
  @Autowired private UtilisateurRepository utilisateurRepository;

  /**
   * Intercepte chaque requête HTTP pour vérifier la présence d’un token JWT
   * valide. Si un token est trouvé et authentifié, configure le contexte de
   * sécurité Spring.
   *
   * @param request la requête HTTP entrante
   * @param response la réponse HTTP à renvoyer
   * @param filterChain la chaîne de filtres à continuer après ce filtre
   * @throws ServletException en cas d'erreur liée au traitement de la requête
   * @throws IOException en cas d'erreur d'entrée/sortie
   */

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    if (jwtService.validateToken(token)) {
      String email = jwtService.getEmailFromToken(token);
      String role = jwtService.getRoleFromToken(token);

      Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);
      if (user != null) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority(role)));

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }
}

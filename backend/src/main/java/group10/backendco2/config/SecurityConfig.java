package group10.backendco2.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * Configuration principale de la sécurité Spring Security pour l'application.
 *
 * Configure :
 * <ul>
 *   <li>la chaîne de filtres de sécurité (SecurityFilterChain),</li>
 *   <li>le filtre d'authentification JWT,</li>
 *   <li>le gestionnaire d'authentification (AuthenticationManager),</li>
 *   <li>la politique CORS pour autoriser les requêtes frontend.</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  /** Filtre personnalisé chargé de l'authentification via JWT. */
  private final JwtAuthenticationFilter jwtAuthFilter;

  /**
   * Constructeur de la classe SecurityConfig.
   *
   * @param jwtAuthFilter le filtre d'authentification JWT
   */

  public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }
  /**
   * Définit un encodeur de mot de passe utilisant l'algorithme BCrypt.
   *
   * @return un bean {@link PasswordEncoder}
   */

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /**
   * Configure la chaîne de filtres de sécurité HTTP pour l'application :
   * <ul>
   *   <li>Désactive CSRF (stateless)</li>
   *   <li>Active CORS avec la config définie</li>
   *   <li>Autorise certains endpoints en public</li>
   *   <li>Protège tous les autres endpoints par authentification JWT</li>
   * </ul>
   *
   * @param http configuration de sécurité HTTP
   * @return un bean {@link SecurityFilterChain}
   * @throws Exception si une erreur de configuration survient
   */

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .authorizeHttpRequests(
            auth
            -> auth.requestMatchers(
                       "/auth/**", "/transports", "/transports/search",
                       "/transports/search/save", "/v3/api-docs/**",
                       "/swagger-ui/**", "/swagger-ui.html", "/webjars/**",
                       "/comparer/", "/swagger-resources/**", "/simulations")
                   .permitAll()
                   .anyRequest()
                   .authenticated())
        .addFilterBefore(jwtAuthFilter,
                         UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Déclare un filtre CORS autorisant les appels frontend (ex. depuis React).
   *
   * @return un bean {@link CorsFilter} avec configuration des origines,
   *     méthodes, et headers autorisés
   */
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173",
                                     "http://192.168.75.53",
                                     "http://192.168.75.53:5173"));
    config.setAllowedMethods(
        List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
  /**
   * Fournit le gestionnaire d'authentification à partir de la configuration
   * Spring.
   *
   * @param config configuration Spring d'authentification
   * @return un bean {@link AuthenticationManager}
   * @throws Exception si une erreur survient lors de la récupération
   */

  @Bean
  public AuthenticationManager
  authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}

package group10.backendco2.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

/**
 * Tests unitaires de la configuration de sécurité {@link SecurityConfig}.
 *
 * Vérifie :
 * <ul>
 *   <li>La configuration du filtre CORS</li>
 *   <li>L'encodage des mots de passe avec BCrypt</li>
 *   <li>La récupération du {@link AuthenticationManager}</li>
 *   <li>La construction de la chaîne de filtres de sécurité</li>
 * </ul>
 */

class SecurityConfigTest {

  /**
   * Instance de {@link JwtAuthenticationFilter} pour simuler le filtre
   * d'authentification JWT.
   */
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Instance de {@link SecurityConfig} à tester.
   */
  private SecurityConfig securityConfig;

  /**
   * Méthode exécutée avant chaque test pour initialiser les instances
   * nécessaires.
   */
  @BeforeEach
  void setUp() {
    jwtAuthenticationFilter = mock(JwtAuthenticationFilter.class);
    securityConfig = new SecurityConfig(jwtAuthenticationFilter);
  }

  /**
   * Vérifie que le bean {@link PasswordEncoder} utilise bien BCrypt et encode
   * correctement.
   */
  @Test
  void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
    PasswordEncoder encoder = securityConfig.passwordEncoder();
    assertNotNull(encoder);
    assertTrue(encoder.matches("password", encoder.encode("password")));
  }
  /**
   * Vérifie que le filtre CORS autorise correctement les origines configurées.
   */
  @Test
  void corsFilter_shouldAllowConfiguredOrigins() throws Exception {
    CorsFilter corsFilter = securityConfig.corsFilter();
    assertNotNull(corsFilter);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("OPTIONS");
    request.addHeader("Origin", "http://localhost:5173");
    request.setRequestURI("/some-endpoint");

    MockHttpServletResponse response = new MockHttpServletResponse();

    corsFilter.doFilter(request, response, (req, res) -> {});

    assertEquals("http://localhost:5173",
                 response.getHeader("Access-Control-Allow-Origin"));
    assertEquals("true",
                 response.getHeader("Access-Control-Allow-Credentials"));
  }
  /**
   * Vérifie que le filtre CORS n'autorise pas les origines non configurées.
   */
  @Test
  void authenticationManager_shouldReturnAuthenticationManager()
      throws Exception {
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    AuthenticationConfiguration config =
        mock(AuthenticationConfiguration.class);
    when(config.getAuthenticationManager()).thenReturn(mockManager);

    AuthenticationManager result = securityConfig.authenticationManager(config);
    assertNotNull(result);
    assertSame(mockManager, result);
  }

  /**
   * Vérifie que la chaîne de filtres de sécurité est construite correctement.
   */
  @Test
  void securityFilterChain_shouldBuildSuccessfully() throws Exception {
    org.springframework.security.config.annotation.web.builders
        .HttpSecurity httpSecurityMock =
        mock(org.springframework.security.config.annotation.web.builders
                 .HttpSecurity.class);
    org.springframework.security.web.DefaultSecurityFilterChain mockChain =
        mock(org.springframework.security.web.DefaultSecurityFilterChain.class);

    when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.sessionManagement(any()))
        .thenReturn(httpSecurityMock);
    when(httpSecurityMock.authorizeHttpRequests(any()))
        .thenReturn(httpSecurityMock);
    when(httpSecurityMock.addFilterBefore(any(), any()))
        .thenReturn(httpSecurityMock);
    when(httpSecurityMock.build()).thenReturn(mockChain);

    SecurityFilterChain chain =
        securityConfig.securityFilterChain(httpSecurityMock);
    assertNotNull(chain);
  }
}
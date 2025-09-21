package group10.backendco2.config;

import group10.backendco2.model.Utilisateur;
import group10.backendco2.repository.UtilisateurRepository;
import group10.backendco2.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;


/**
 * Test unitaire pour la classe {@link JwtAuthenticationFilter}.
 * <p>
 * Cette classe teste le filtre d'authentification JWT pour s'assurer qu'il fonctionne correctement
 * dans différents scénarios.
 */

class JwtAuthenticationFilterTest {

    /**
     * Instance de {@link JwtAuthenticationFilter} à tester.
     */
    private JwtAuthenticationFilter filter;

    /**
     * Instance de {@link JwtService} pour simuler le service JWT.
     */
    private JwtService jwtService;

    /**
     * Instance de {@link UtilisateurRepository} pour simuler le référentiel des utilisateurs.
     */
    private UtilisateurRepository utilisateurRepository;

    /**
     * Instance de {@link HttpServletRequest} pour simuler la requête HTTP.
     */
    private HttpServletRequest request;

    /**
     * Instance de {@link HttpServletResponse} pour simuler la réponse HTTP.
     */
    private HttpServletResponse response;

    /**
     * Instance de {@link FilterChain} pour simuler la chaîne de filtres.
     */
    private FilterChain filterChain;


    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * <p>
     * Cette méthode crée des instances simulées de {@link JwtService}, {@link UtilisateurRepository},
     * {@link HttpServletRequest}, {@link HttpServletResponse} et {@link FilterChain}.
     */
    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        filter = new JwtAuthenticationFilter();
        try {
            var jwtField = JwtAuthenticationFilter.class.getDeclaredField("jwtService");
            jwtField.setAccessible(true);
            jwtField.set(filter, jwtService);
            var userRepoField = JwtAuthenticationFilter.class.getDeclaredField("utilisateurRepository");
            userRepoField.setAccessible(true);
            userRepoField.set(filter, utilisateurRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }


    /**
     * Teste le filtre d'authentification JWT lorsque l'en-tête d'autorisation est absent.
     * <p>
     * Dans ce cas, le filtre doit simplement passer à la chaîne de filtres sans effectuer
     * d'authentification.
     *
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Teste le filtre d'authentification JWT lorsque l'en-tête d'autorisation n'est pas de type Bearer.
     * <p>
     * Dans ce cas, le filtre doit simplement passer à la chaîne de filtres sans effectuer
     * d'authentification.
     *
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testAuthorizationHeaderNotBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic abcdef");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Teste le filtre d'authentification JWT lorsque le token est invalide.
     * <p>
     * Dans ce cas, le filtre doit simplement passer à la chaîne de filtres sans effectuer
     * d'authentification.
     *
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtService.validateToken("invalidtoken")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Teste le filtre d'authentification JWT lorsque le token est valide mais que l'utilisateur n'est pas trouvé.
     * <p>
     * Dans ce cas, le filtre doit simplement passer à la chaîne de filtres sans effectuer
     * d'authentification.
     *
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testValidTokenUserNotFound() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtService.validateToken("validtoken")).thenReturn(true);
        when(jwtService.getEmailFromToken("validtoken")).thenReturn("user@example.com");
        when(jwtService.getRoleFromToken("validtoken")).thenReturn("ROLE_USER");
        when(utilisateurRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Teste le filtre d'authentification JWT lorsque le token est valide et que l'utilisateur est trouvé.
     * <p>
     * Dans ce cas, le filtre doit authentifier l'utilisateur et le stocker dans le contexte de sécurité.
     *
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testValidTokenUserFound() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtService.validateToken("validtoken")).thenReturn(true);
        when(jwtService.getEmailFromToken("validtoken")).thenReturn("user@example.com");
        when(jwtService.getRoleFromToken("validtoken")).thenReturn("ROLE_USER");
        Utilisateur user = new Utilisateur();
        when(utilisateurRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        try (MockedStatic<SecurityContextHolder> contextHolder = Mockito.mockStatic(SecurityContextHolder.class, Mockito.CALLS_REAL_METHODS)) {
            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
            assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
            assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
    }
}
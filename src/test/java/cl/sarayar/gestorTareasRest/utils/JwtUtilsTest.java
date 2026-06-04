package cl.sarayar.gestorTareasRest.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import cl.sarayar.gestorTareasRest.config.auth.UserDetailsImpl;
import cl.sarayar.gestorTareasRest.entities.Usuario;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private final String secret = "gestorSecretKeyForTestingPurposesWhichHasToHaveEnoughBytesToAvoidValidationErrors1234567890";
    private final int expirationMs = 3600000;

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", expirationMs);
    }

    @Test
    public void testGetSigningKey() {
        assertEquals(secret, jwtUtils.getSigningKey());
    }

    @Test
    public void testGenerateAndValidateToken_Success() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        UserDetailsImpl principal = new UserDetailsImpl(usuario);

        when(authentication.getPrincipal()).thenReturn(principal);
        doReturn(Collections.emptyList()).when(authentication).getAuthorities();

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("sarayar@skynux.cl", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_InvalidSignature() {
        String invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXJheWFyQHNreW51eC5jbCIsIkNMQVZFUyI6IiIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoyNzAwMDAwMDAwfQ.invalidSignature";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    public void testValidateJwtToken_Malformed() {
        assertFalse(jwtUtils.validateJwtToken("malformedToken"));
    }

    @Test
    public void testValidateJwtToken_Expired() {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000);

        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        UserDetailsImpl principal = new UserDetailsImpl(usuario);

        when(authentication.getPrincipal()).thenReturn(principal);
        doReturn(Collections.emptyList()).when(authentication).getAuthorities();

        String token = jwtUtils.generateJwtToken(authentication);

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_Unsupported() {
        // Token with alg: none and no signature to trigger UnsupportedJwtException
        String unsignedToken = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJzYXJheWFyQHNreW51eC5jbCJ9.";
        assertFalse(jwtUtils.validateJwtToken(unsignedToken));
    }

    @Test
    public void testValidateJwtToken_EmptyOrNull() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }
}

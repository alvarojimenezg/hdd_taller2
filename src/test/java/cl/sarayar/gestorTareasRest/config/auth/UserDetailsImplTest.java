package cl.sarayar.gestorTareasRest.config.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cl.sarayar.gestorTareasRest.entities.Usuario;

public class UserDetailsImplTest {

    @Test
    public void testUserDetailsMethods() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        assertEquals("clave", userDetails.getPassword());
        assertEquals("sarayar@skynux.cl", userDetails.getUsername());
        assertEquals(usuario, userDetails.getUsuario());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEqualsAndHashCode() {
        Usuario u1 = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        Usuario u2 = new Usuario("1", "Admin Different Name", "sarayar@skynux.cl", "clave", 1);
        Usuario u3 = new Usuario("2", "User", "user@skynux.cl", "clave", 1);

        UserDetailsImpl userDetails1 = new UserDetailsImpl(u1);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(u2);
        UserDetailsImpl userDetails3 = new UserDetailsImpl(u3);

        assertTrue(userDetails1.equals(userDetails1));
        assertFalse(userDetails1.equals(null));
        assertFalse(userDetails1.equals("Not a UserDetailsImpl"));
        assertTrue(userDetails1.equals(userDetails2));
        assertFalse(userDetails1.equals(userDetails3));
    }
}

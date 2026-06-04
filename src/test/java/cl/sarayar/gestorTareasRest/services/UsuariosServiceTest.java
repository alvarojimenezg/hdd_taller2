package cl.sarayar.gestorTareasRest.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import cl.sarayar.gestorTareasRest.entities.Usuario;
import cl.sarayar.gestorTareasRest.repositories.UsuariosRepository;
import cl.sarayar.gestorTareasRest.config.auth.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class UsuariosServiceTest {

    @Mock
    private UsuariosRepository usRepo;

    @InjectMocks
    private UsuariosServiceImpl usuariosService;

    @Test
    public void testSave() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        when(usRepo.save(usuario)).thenReturn(usuario);

        Usuario result = usuariosService.save(usuario);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Admin", result.getNombre());
        assertEquals("sarayar@skynux.cl", result.getCorreo());
        assertEquals("clave", result.getClave());
        assertEquals(1, result.getEstado());
        verify(usRepo, times(1)).save(usuario);
    }

    @Test
    public void testGetAll() {
        Usuario u1 = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        Usuario u2 = new Usuario("2", "User", "user@skynux.cl", "clave", 1);
        when(usRepo.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<Usuario> result = usuariosService.getAll();

        assertEquals(2, result.size());
        assertEquals(u1, result.get(0));
        assertEquals(u2, result.get(1));
        verify(usRepo, times(1)).findAll();
    }

    @Test
    public void testFindByCorreo_Found() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        when(usRepo.findByCorreo("sarayar@skynux.cl")).thenReturn(Optional.of(usuario));

        Usuario result = usuariosService.findByCorreo("sarayar@skynux.cl");

        assertNotNull(result);
        assertEquals(usuario, result);
    }

    @Test
    public void testFindByCorreo_NotFound() {
        when(usRepo.findByCorreo("notfound@skynux.cl")).thenReturn(Optional.empty());

        Usuario result = usuariosService.findByCorreo("notfound@skynux.cl");

        assertNull(result);
    }

    @Test
    public void testFindById_Found() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        when(usRepo.findById("1")).thenReturn(Optional.of(usuario));

        Usuario result = usuariosService.findById("1");

        assertNotNull(result);
        assertEquals(usuario, result);
    }

    @Test
    public void testFindById_NotFound() {
        when(usRepo.findById("999")).thenReturn(Optional.empty());

        Usuario result = usuariosService.findById("999");

        assertNull(result);
    }

    @Test
    public void testLoadUserByUsername_Found() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        when(usRepo.findByCorreo("sarayar@skynux.cl")).thenReturn(Optional.of(usuario));

        UserDetails result = usuariosService.loadUserByUsername("sarayar@skynux.cl");

        assertNotNull(result);
        assertTrue(result instanceof UserDetailsImpl);
        assertEquals("sarayar@skynux.cl", result.getUsername());
        assertEquals(usuario, ((UserDetailsImpl) result).getUsuario());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(usRepo.findByCorreo("notfound@skynux.cl")).thenReturn(Optional.empty());

        UserDetails result = usuariosService.loadUserByUsername("notfound@skynux.cl");

        assertNull(result);
    }

    @Test
    public void testExistsByCorreo() {
        when(usRepo.existsByCorreo("sarayar@skynux.cl")).thenReturn(true);
        when(usRepo.existsByCorreo("notfound@skynux.cl")).thenReturn(false);

        assertTrue(usuariosService.existsByCorreo("sarayar@skynux.cl"));
        assertFalse(usuariosService.existsByCorreo("notfound@skynux.cl"));
    }
}

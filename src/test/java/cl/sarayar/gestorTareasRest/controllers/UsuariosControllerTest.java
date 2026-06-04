package cl.sarayar.gestorTareasRest.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.sarayar.gestorTareasRest.config.auth.dto.MessageResponse;
import cl.sarayar.gestorTareasRest.entities.Usuario;
import cl.sarayar.gestorTareasRest.services.UsuariosService;
import cl.sarayar.gestorTareasRest.utils.JwtUtils;

@ExtendWith(MockitoExtension.class)
public class UsuariosControllerTest {

    @Mock
    private UsuariosService usService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UsuariosController usuariosController;

    @Test
    public void testAuthenticateUser() {
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);

        ResponseEntity<?> response = usuariosController.authenticateUser(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    public void testRegisterUser_NewUser() {
        Usuario usuario = new Usuario(null, "Admin", "sarayar@skynux.cl", "clave", 1);
        Usuario savedUsuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);

        when(usService.existsByCorreo("sarayar@skynux.cl")).thenReturn(false);
        when(usService.save(any(Usuario.class))).thenReturn(savedUsuario);

        ResponseEntity<?> response = usuariosController.registerUser(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedUsuario, response.getBody());
    }

    @Test
    public void testRegisterUser_AlreadyExists() {
        Usuario usuario = new Usuario(null, "Admin", "sarayar@skynux.cl", "clave", 1);

        when(usService.existsByCorreo("sarayar@skynux.cl")).thenReturn(true);

        ResponseEntity<?> response = usuariosController.registerUser(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Usuario ya existe!", ((MessageResponse) response.getBody()).getMensaje());
    }

    @Test
    public void testActualizarUsuario_Success() {
        Usuario usuario = new Usuario("1", "Admin Updated", "sarayar@skynux.cl", "clave", 1);
        Usuario usuarioOriginal = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);

        when(usService.findById("1")).thenReturn(usuarioOriginal);
        when(usService.findByCorreo("sarayar@skynux.cl")).thenReturn(usuarioOriginal);
        when(usService.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuariosController.actualizarUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    public void testActualizarUsuario_EmailNotTakenNull() {
        Usuario usuario = new Usuario("1", "Admin Updated", "newemail@skynux.cl", "clave", 1);
        Usuario usuarioOriginal = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);

        when(usService.findById("1")).thenReturn(usuarioOriginal);
        when(usService.findByCorreo("newemail@skynux.cl")).thenReturn(null);
        when(usService.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuariosController.actualizarUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    public void testActualizarUsuario_EmailTaken() {
        Usuario usuario = new Usuario("1", "Admin Updated", "taken@skynux.cl", "clave", 1);
        Usuario usuarioOriginal = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        Usuario otherUsuario = new Usuario("2", "Other", "taken@skynux.cl", "clave", 1);

        when(usService.findById("1")).thenReturn(usuarioOriginal);
        when(usService.findByCorreo("taken@skynux.cl")).thenReturn(otherUsuario);

        ResponseEntity<?> response = usuariosController.actualizarUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Correo se encuentra utilizado!", ((MessageResponse) response.getBody()).getMensaje());
    }

    @Test
    public void testGetAll() {
        Usuario u1 = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        Usuario u2 = new Usuario("2", "User", "user@skynux.cl", "clave", 1);

        when(usService.getAll()).thenReturn(Arrays.asList(u1, u2));

        List<Usuario> response = usuariosController.getAll();

        assertEquals(2, response.size());
        assertEquals(u1, response.get(0));
        assertEquals(u2, response.get(1));
    }
}

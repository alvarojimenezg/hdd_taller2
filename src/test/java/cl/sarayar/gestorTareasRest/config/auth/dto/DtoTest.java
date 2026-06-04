package cl.sarayar.gestorTareasRest.config.auth.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cl.sarayar.gestorTareasRest.entities.Usuario;

public class DtoTest {

    @Test
    public void testJwtResponse() {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken("myToken");
        Usuario usuario = new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1);
        jwtResponse.setUsuario(usuario);

        assertEquals("myToken", jwtResponse.getToken());
        assertEquals(usuario, jwtResponse.getUsuario());
        assertNotNull(jwtResponse.toString());

        JwtResponse jwtResponse2 = new JwtResponse("anotherToken", usuario);
        assertEquals("anotherToken", jwtResponse2.getToken());
        assertEquals(usuario, jwtResponse2.getUsuario());
    }

    @Test
    public void testMessageResponse() {
        MessageResponse messageResponse = new MessageResponse("Hello message");
        assertEquals("Hello message", messageResponse.getMensaje());

        messageResponse.setMensaje("New message");
        assertEquals("New message", messageResponse.getMensaje());
        assertNotNull(messageResponse.toString());
    }
}

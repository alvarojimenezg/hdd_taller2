package cl.sarayar.gestorTareasRest.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class EntitiesTest {

    @Test
    public void testUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNombre("Alvaro");
        usuario.setCorreo("alvaro@sarayar.cl");
        usuario.setClave("pwd");
        usuario.setEstado(1);

        assertEquals("1", usuario.getId());
        assertEquals("Alvaro", usuario.getNombre());
        assertEquals("alvaro@sarayar.cl", usuario.getCorreo());
        assertEquals("pwd", usuario.getClave());
        assertEquals(1, usuario.getEstado());
        assertNotNull(usuario.toString());

        Usuario usuario2 = new Usuario("2", "User", "user@sarayar.cl", "pwd", 1);
        assertEquals("2", usuario2.getId());
        assertEquals("User", usuario2.getNombre());
    }

    @Test
    public void testTarea() {
        Tarea tarea = new Tarea();
        tarea.setId("1");
        tarea.setIdentificador(100L);
        tarea.setDescripcion("My Task");
        LocalDateTime now = LocalDateTime.now();
        tarea.setFechaCreacion(now);
        tarea.setVigente(true);

        assertEquals("1", tarea.getId());
        assertEquals(100L, tarea.getIdentificador());
        assertEquals("My Task", tarea.getDescripcion());
        assertEquals(now, tarea.getFechaCreacion());
        assertTrue(tarea.isVigente());
        assertNotNull(tarea.toString());
    }

    @Test
    public void testSecuencia() {
        Secuencia secuencia = new Secuencia();
        secuencia.setId("tasks_seq");
        secuencia.setSeq(50L);

        assertEquals("tasks_seq", secuencia.getId());
        assertEquals(50L, secuencia.getSeq());
    }
}

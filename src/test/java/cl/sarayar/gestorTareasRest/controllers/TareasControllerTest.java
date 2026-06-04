package cl.sarayar.gestorTareasRest.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.sarayar.gestorTareasRest.entities.Tarea;
import cl.sarayar.gestorTareasRest.services.TareasService;

@ExtendWith(MockitoExtension.class)
public class TareasControllerTest {

    @Mock
    private TareasService tareasService;

    @InjectMocks
    private TareasController tareasController;

    @Test
    public void testGetAll() {
        Tarea t1 = new Tarea();
        t1.setId("1");
        t1.setDescripcion("Task 1");
        t1.setVigente(true);

        Tarea t2 = new Tarea();
        t2.setId("2");
        t2.setDescripcion("Task 2");
        t2.setVigente(false);

        when(tareasService.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Tarea> response = tareasController.getAll();

        assertEquals(2, response.size());
        assertEquals(t1, response.get(0));
        assertEquals(t2, response.get(1));
    }

    @Test
    public void testSave() {
        Tarea inputTarea = new Tarea();
        inputTarea.setDescripcion("Task 1");
        inputTarea.setVigente(true);

        Tarea savedTarea = new Tarea();
        savedTarea.setId("1");
        savedTarea.setDescripcion("Task 1");
        savedTarea.setVigente(true);
        savedTarea.setFechaCreacion(LocalDateTime.now());

        when(tareasService.save(any(Tarea.class))).thenReturn(savedTarea);

        ResponseEntity<Tarea> response = tareasController.save(inputTarea);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedTarea, response.getBody());
    }

    @Test
    public void testUpdate_Success() {
        Tarea inputTarea = new Tarea();
        inputTarea.setId("1");
        inputTarea.setDescripcion("Updated Task");
        inputTarea.setVigente(false);

        Tarea originalTarea = new Tarea();
        originalTarea.setId("1");
        originalTarea.setDescripcion("Task 1");
        originalTarea.setVigente(true);

        Tarea updatedTarea = new Tarea();
        updatedTarea.setId("1");
        updatedTarea.setDescripcion("Updated Task");
        updatedTarea.setVigente(false);

        when(tareasService.findById("1")).thenReturn(originalTarea);
        when(tareasService.save(any(Tarea.class))).thenReturn(updatedTarea);

        ResponseEntity<Tarea> response = tareasController.update(inputTarea);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTarea, response.getBody());
    }

    @Test
    public void testUpdate_NotFound() {
        Tarea inputTarea = new Tarea();
        inputTarea.setId("999");
        inputTarea.setDescripcion("Updated Task");
        inputTarea.setVigente(false);

        when(tareasService.findById("999")).thenReturn(null);

        ResponseEntity<Tarea> response = tareasController.update(inputTarea);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDelete_Success() {
        when(tareasService.remove("1")).thenReturn(true);

        ResponseEntity<Boolean> response = tareasController.delete("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testDelete_Failure() {
        doThrow(new RuntimeException("Database error")).when(tareasService).remove("1");

        ResponseEntity<Boolean> response = tareasController.delete("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody());
    }
}

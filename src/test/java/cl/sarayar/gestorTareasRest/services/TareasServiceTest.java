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

import cl.sarayar.gestorTareasRest.entities.Tarea;
import cl.sarayar.gestorTareasRest.repositories.TareasRepository;

@ExtendWith(MockitoExtension.class)
public class TareasServiceTest {

    @Mock
    private TareasRepository tareasRepository;

    @InjectMocks
    private TareasServiceImpl tareasService;

    @Test
    public void testFindAll() {
        Tarea t1 = new Tarea();
        t1.setId("1");
        t1.setDescripcion("Task 1");
        t1.setVigente(true);

        Tarea t2 = new Tarea();
        t2.setId("2");
        t2.setDescripcion("Task 2");
        t2.setVigente(false);

        when(tareasRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Tarea> result = tareasService.findAll();

        assertEquals(2, result.size());
        assertEquals(t1, result.get(0));
        assertEquals(t2, result.get(1));
        verify(tareasRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        Tarea tarea = new Tarea();
        tarea.setId("1");
        tarea.setDescripcion("Task 1");
        tarea.setVigente(true);

        when(tareasRepository.save(tarea)).thenReturn(tarea);

        Tarea result = tareasService.save(tarea);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Task 1", result.getDescripcion());
        assertTrue(result.isVigente());
        verify(tareasRepository, times(1)).save(tarea);
    }

    @Test
    public void testRemove_Success() {
        String id = "1";
        doNothing().when(tareasRepository).deleteById(id);

        boolean result = tareasService.remove(id);

        assertTrue(result);
        verify(tareasRepository, times(1)).deleteById(id);
    }

    @Test
    public void testRemove_Failure() {
        String id = null;
        doThrow(new IllegalArgumentException("id cannot be null")).when(tareasRepository).deleteById(id);

        boolean result = tareasService.remove(id);

        assertFalse(result);
        verify(tareasRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindById_Found() {
        String id = "1";
        Tarea tarea = new Tarea();
        tarea.setId(id);
        tarea.setDescripcion("Task 1");

        when(tareasRepository.findById(id)).thenReturn(Optional.of(tarea));

        Tarea result = tareasService.findById(id);

        assertNotNull(result);
        assertEquals(tarea, result);
    }

    @Test
    public void testFindById_NotFound() {
        String id = "999";
        when(tareasRepository.findById(id)).thenReturn(Optional.empty());

        Tarea result = tareasService.findById(id);

        assertNull(result);
    }
}

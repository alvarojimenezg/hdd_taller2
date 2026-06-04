package cl.sarayar.gestorTareasRest.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import cl.sarayar.gestorTareasRest.entities.Tarea;
import cl.sarayar.gestorTareasRest.services.GeneradorSecuenciaService;

@ExtendWith(MockitoExtension.class)
public class TareasModelListenerTest {

    @Mock
    private GeneradorSecuenciaService generador;

    @InjectMocks
    private TareasModelListener listener;

    @Test
    public void testOnBeforeConvert_IdentificadorLessThanOne() {
        Tarea tarea = new Tarea();
        tarea.setIdentificador(0);
        tarea.setDescripcion("Task test");

        when(generador.generadorSecuencia(Tarea.NOMBRE_SECUENCIA)).thenReturn(42L);

        BeforeConvertEvent<Tarea> event = new BeforeConvertEvent<>(tarea, "tareas");
        listener.onBeforeConvert(event);

        assertEquals(42L, tarea.getIdentificador());
        verify(generador, times(1)).generadorSecuencia(Tarea.NOMBRE_SECUENCIA);
    }

    @Test
    public void testOnBeforeConvert_IdentificadorAlreadySet() {
        Tarea tarea = new Tarea();
        tarea.setIdentificador(5L);
        tarea.setDescripcion("Task test");

        BeforeConvertEvent<Tarea> event = new BeforeConvertEvent<>(tarea, "tareas");
        listener.onBeforeConvert(event);

        assertEquals(5L, tarea.getIdentificador());
        verify(generador, never()).generadorSecuencia(anyString());
    }
}

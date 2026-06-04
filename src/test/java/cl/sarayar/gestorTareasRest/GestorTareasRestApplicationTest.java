package cl.sarayar.gestorTareasRest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.test.util.ReflectionTestUtils;

import cl.sarayar.gestorTareasRest.entities.Usuario;
import cl.sarayar.gestorTareasRest.services.UsuariosService;

@ExtendWith(MockitoExtension.class)
public class GestorTareasRestApplicationTest {

    @Mock
    private UsuariosService usService;

    @InjectMocks
    private GestorTareasRestApplication application;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(application, "usService", usService);
    }

    @Test
    public void testRun_DatabaseNotEmpty() throws Exception {
        List<Usuario> list = new ArrayList<>();
        list.add(new Usuario("1", "Admin", "sarayar@skynux.cl", "clave", 1));
        when(usService.getAll()).thenReturn(list);

        application.run();

        verify(usService, never()).save(any(Usuario.class));
    }

    @Test
    public void testRun_DatabaseEmpty() throws Exception {
        when(usService.getAll()).thenReturn(Collections.emptyList());
        when(usService.save(any(Usuario.class))).thenReturn(new Usuario());

        application.run();

        // Capture and assert that the seeded administrator properties make contextual sense
        ArgumentCaptor<Usuario> userCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usService, times(1)).save(userCaptor.capture());
        
        Usuario adminSeeded = userCaptor.getValue();
        assertNotNull(adminSeeded);
        assertEquals("Admin", adminSeeded.getNombre());
        assertEquals("sarayar@skynux.cl", adminSeeded.getCorreo());
    }

    @Test
    public void testRun_ExceptionThrown() {
        when(usService.getAll()).thenThrow(new RuntimeException("Database down"));

        assertDoesNotThrow(() -> application.run());
    }

    @Test
    public void testMain() {
        try (MockedStatic<SpringApplication> springAppMock = mockStatic(SpringApplication.class)) {
            springAppMock.when(() -> SpringApplication.run(GestorTareasRestApplication.class, new String[]{}))
                         .thenReturn(null);

            GestorTareasRestApplication.main(new String[]{});

            springAppMock.verify(() -> SpringApplication.run(GestorTareasRestApplication.class, new String[]{}), times(1));
        }
    }
}

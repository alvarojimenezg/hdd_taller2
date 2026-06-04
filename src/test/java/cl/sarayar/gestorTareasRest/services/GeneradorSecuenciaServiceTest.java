package cl.sarayar.gestorTareasRest.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import cl.sarayar.gestorTareasRest.entities.Secuencia;

@ExtendWith(MockitoExtension.class)
public class GeneradorSecuenciaServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    private GeneradorSecuenciaServiceImpl generadorSecuenciaService;

    @BeforeEach
    public void setUp() {
        generadorSecuenciaService = new GeneradorSecuenciaServiceImpl(mongoOperations);
    }

    @Test
    public void testGeneradorSecuencia_NotNull() {
        Secuencia secuencia = new Secuencia();
        secuencia.setId("tareas_secuencia");
        secuencia.setSeq(10L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Secuencia.class)
        )).thenReturn(secuencia);

        long result = generadorSecuenciaService.generadorSecuencia("tareas_secuencia");

        assertEquals(10L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Secuencia.class)
        );
    }

    @Test
    public void testGeneradorSecuencia_Null() {
        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Secuencia.class)
        )).thenReturn(null);

        long result = generadorSecuenciaService.generadorSecuencia("tareas_secuencia");

        assertEquals(1L, result);
    }
}

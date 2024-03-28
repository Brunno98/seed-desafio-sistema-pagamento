package br.com.brunno.yfood.infrastructure.validators;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

public class ExistsValidatorTest {

    EntityManager entityManager = Mockito.mock(EntityManager.class);
    ExistsValidator validator = new ExistsValidator(entityManager);

    @DisplayName("Quando o valor alvo for nulo, não se deve fazer validação")
    @Test
    void nullValue() {
        boolean result = validator.isValid(null, null);

        Assertions.assertThat(result).isTrue();
        Mockito.verify(entityManager, Mockito.never()).createQuery(Mockito.anyString());
    }

    @DisplayName("Quando a entidade existir então o validador deve retornar valido")
    @Test
    void exists() {
        Query query = Mockito.mock(Query.class);
        Mockito.doReturn(query).when(entityManager).createQuery(Mockito.anyString());
        Mockito.doReturn(query).when(query).setParameter("value", 1L);
        Mockito.doReturn(List.of(new Object())).when(query).getResultList();
        ReflectionTestUtils.setField(validator, "klass", Object.class);
        ReflectionTestUtils.setField(validator, "domainField", "id");

        boolean result = validator.isValid(1L, null);

        Assertions.assertThat(result).isTrue();
        Mockito.verify(entityManager).createQuery(Mockito.anyString());
    }

    @DisplayName("Quando a entidade não existir então o validador deve retornar invalido")
    @Test
    void notFound() {
        Query query = Mockito.mock(Query.class);
        Mockito.doReturn(query).when(entityManager).createQuery(Mockito.anyString());
        Mockito.doReturn(query).when(query).setParameter("value", 1L);
        Mockito.doReturn(Collections.emptyList()).when(query).getResultList();
        ReflectionTestUtils.setField(validator, "klass", Object.class);
        ReflectionTestUtils.setField(validator, "domainField", "id");

        boolean result = validator.isValid(1L, null);

        Assertions.assertThat(result).isFalse();
        Mockito.verify(entityManager).createQuery(Mockito.anyString());
    }
}

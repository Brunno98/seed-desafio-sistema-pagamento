package br.com.brunno.yfood.infrastructure.validators;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/* carga cognitiva:
    - @Exists
 */

@Component
public class ExistsValidator implements ConstraintValidator<Exists, Long> {

    private final EntityManager entityManager;

    @Autowired
    public ExistsValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Class<?> klass;
    private String domainField;

    @Override
    public void initialize(Exists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.klass = constraintAnnotation.domain();
        this.domainField = constraintAnnotation.domainField();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) return true;

        List resultList = entityManager.createQuery("SELECT a FROM " + klass.getName() + " a where a." + domainField + " = :value")
                .setParameter("value", value)
                .getResultList();

        return !resultList.isEmpty();
    }
}

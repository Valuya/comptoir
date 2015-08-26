package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.api.domain.company.WithId;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class NoIdValidator implements ConstraintValidator<NoId, WithId> {

    @Override
    public void initialize(NoId constraintAnnotation) {
    }

    @Override
    public boolean isValid(WithId withId, ConstraintValidatorContext constraintValidatorContext) {
        if (withId == null) {
            return true;
        }
        Long id = withId.getId();
        return id == null;
    }
}

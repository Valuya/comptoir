package be.valuya.comptoir.ws.api.validation;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.ws.api.validation.NoId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
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

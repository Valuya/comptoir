package be.valuya.comptoir.ws.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = NoIdValidator.class)
public @interface NoId {

    String message() default "Object is expected to have no id for this operation.";

    Class[] groups() default {};

    Class[] payload() default {};

}

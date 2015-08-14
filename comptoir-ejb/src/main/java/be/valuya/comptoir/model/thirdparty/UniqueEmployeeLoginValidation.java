package be.valuya.comptoir.model.thirdparty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmployeeLoginValidator.class)
@Documented
public @interface UniqueEmployeeLoginValidation {

    String message() default "Non-unique login.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

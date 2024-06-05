package am.itspace.jobboard.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AgeConstraintValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface MinAge {
    String message() ;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

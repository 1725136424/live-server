package site.wanjiahao.live.valid.custom;

import site.wanjiahao.live.valid.validatior.CheckNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {CheckNumberValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Number {

    String message() default "{javax.validation.constraints.number.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

    int max() default 9999;

    int min() default 1;
}
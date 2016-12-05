package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Никита on 03.12.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentA {
    String name() default "";
}

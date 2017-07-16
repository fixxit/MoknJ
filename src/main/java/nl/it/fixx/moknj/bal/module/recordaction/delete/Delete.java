package nl.it.fixx.moknj.bal.module.recordaction.delete;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.it.fixx.moknj.bal.module.recordaction.Intercept;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {

    Intercept[] intercepts();
}

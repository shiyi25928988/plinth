package yi.shi.plinth.annotation.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCache {
    String name();
    long expire() default 0;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}

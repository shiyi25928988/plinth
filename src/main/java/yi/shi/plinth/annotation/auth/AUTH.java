package yi.shi.plinth.annotation.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AUTH {

    String[] andRole() default {};

    String[] orRole() default {};

    String authUrl() default "";

}

import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
public @interface MinLength {
    int min() default 3;
}

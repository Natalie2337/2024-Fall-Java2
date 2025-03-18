import java.lang.annotation.*;

// duplicate annotation

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CustomValidations.class)
public @interface CustomValidation{
    Rule rule();
}

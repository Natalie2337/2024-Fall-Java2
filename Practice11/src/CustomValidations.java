import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidations {
    CustomValidation[] value();
}

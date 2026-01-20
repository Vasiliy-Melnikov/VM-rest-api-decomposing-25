package annotations;

import extensions.LoginExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(LoginExtension.class)
public @interface WithLogin {
}


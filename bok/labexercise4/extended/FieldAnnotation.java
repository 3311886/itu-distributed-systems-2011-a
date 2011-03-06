package bok.labexercise4.extended;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) public @interface FieldAnnotation {
    public String name();
    public String value();
    public boolean isKey() default false; 
}

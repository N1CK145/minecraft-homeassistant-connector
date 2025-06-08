package io.github.n1ck145.redhook.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface ActionField {
    Object description = null;
    String label() default "";
    String description() default "";
    String defaultValue() default "";
    Material icon() default Material.PAPER;
    boolean required() default false;
    boolean hidden() default false;
}

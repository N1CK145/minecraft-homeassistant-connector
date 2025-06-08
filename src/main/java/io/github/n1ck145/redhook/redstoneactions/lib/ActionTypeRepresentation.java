package io.github.n1ck145.redhook.redstoneactions.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionTypeRepresentation {
    /**
     * The material to use as the icon for this action type
     */
    Material icon() default Material.BARRIER;

    /**
     * The description of the action type
     */
    String description() default "";
}

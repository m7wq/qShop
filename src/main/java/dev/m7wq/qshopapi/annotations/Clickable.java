package dev.m7wq.qshopapi.annotations;

import dev.m7wq.qshopapi.annotations.enums.ClickPurpose;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface Clickable {
    ClickPurpose purpose();
    int forSlot() default -1;
}

package dev.m7wq.qshopapi.annotations.settings;

import dev.m7wq.qshopapi.annotations.settings.enums.ShopStatus;
import org.bukkit.Sound;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Settings {

    boolean cancelClickEvent() default true;
    ShopStatus status() default ShopStatus.DIRECT;
}

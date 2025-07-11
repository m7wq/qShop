package dev.m7wq.qshopapi.annotations;

import com.avaje.ebean.validation.NotNull;
import dev.m7wq.qshopapi.main.enums.Capacity;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Shop {

    @NotNull String title();
    @NotNull Capacity capacity();

}

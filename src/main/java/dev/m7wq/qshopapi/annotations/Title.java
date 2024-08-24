package dev.m7wq.qshopapi.annotations;

import com.avaje.ebean.validation.NotNull;

public @interface Title {
    @NotNull String title();
}

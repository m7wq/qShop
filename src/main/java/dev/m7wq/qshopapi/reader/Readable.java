package dev.m7wq.qshopapi.reader;

import dev.m7wq.qshopapi.storage.Inputs;
import org.bukkit.plugin.Plugin;

public interface Readable<V> {

    V read(Object instance,Class<?> clazz, Inputs inputs, Plugin plugin);

}

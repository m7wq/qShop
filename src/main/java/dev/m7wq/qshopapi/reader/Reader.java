package dev.m7wq.qshopapi.reader;

import dev.m7wq.qshopapi.ShopAPI;
import dev.m7wq.qshopapi.storage.Edits;
import dev.m7wq.qshopapi.storage.Inputs;
import org.bukkit.plugin.Plugin;

public interface Reader<V> {

    V read(Object instance, Class<?> clazz, ShopAPI api, Plugin plugin);

}

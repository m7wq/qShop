package dev.m7wq.qshopapi.listeners;

import dev.m7wq.qshopapi.entity.Item;
import org.bukkit.entity.Player;


public interface ItemListener {

    default void onSelect(Player player, Item item){}

    default void onUnSelect(Player player, Item item){}

    default void onClick(Player player, Item item){}

}

package dev.m7wq.qshopapi.main.behaviors;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface Clickable {


    void click(InventoryClickEvent event);
}

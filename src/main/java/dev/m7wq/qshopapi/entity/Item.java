package dev.m7wq.qshopapi.entity;

import dev.m7wq.qshopapi.listeners.ItemListener;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;
import dev.m7wq.qshopapi.listeners.enums.StatusDisplay;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Builder
@Data
public class Item {

    String name;
    List<String> lore;
    Material type;
    int price;
    @Nullable int slot;
    ItemListener listener;
    StatusDisplay display;




}

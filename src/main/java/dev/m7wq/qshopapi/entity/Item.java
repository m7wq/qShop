package dev.m7wq.qshopapi.entity;

import dev.m7wq.qshopapi.main.behaviors.Clickable;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Builder
@Data
public class Item {

    Clickable clickable;
    String name;
    List<String> lore;
    Material type;
    int price;
    @Nullable int slot;

}

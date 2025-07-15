package dev.m7wq.qshopapi.main;

import dev.m7wq.qshopapi.annotations.settings.entity.ShopSettings;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.main.enums.Capacity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ShopInterface {

    String title;
    Capacity capacity;
    List<Item> items;
    List<ShopInterface> subShops;
    ShopSettings settings;

}

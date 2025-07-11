package dev.m7wq.qshopapi.storage;

import dev.m7wq.qshopapi.main.ShopInterface;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Shops {
    Set<ShopInterface> shops = new HashSet<>();
}

package dev.m7wq.qshopapi.extra;

import dev.m7wq.qshopapi.entity.Item;

@FunctionalInterface
public interface Editable {

    String replace(String string, Item item);
}

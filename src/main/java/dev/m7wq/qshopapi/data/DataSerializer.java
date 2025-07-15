package dev.m7wq.qshopapi.data;

import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;

import java.util.HashMap;

public abstract class DataSerializer {

    public abstract void serialize(HashMap<String,HashMap<Item, ItemStatus>> map);

    public abstract HashMap<String,HashMap<Item, ItemStatus>> deserialize();


}

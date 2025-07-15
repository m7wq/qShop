package dev.m7wq.qshopapi.reader;


import dev.m7wq.qshopapi.ActionBase;
import dev.m7wq.qshopapi.ShopAPI;
import dev.m7wq.qshopapi.annotations.*;
import dev.m7wq.qshopapi.annotations.enums.ClickPurpose;
import dev.m7wq.qshopapi.annotations.settings.Settings;
import dev.m7wq.qshopapi.annotations.settings.entity.ShopSettings;
import dev.m7wq.qshopapi.annotations.settings.enums.ShopStatus;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.ItemListener;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.m7wq.qshopapi.main.enums.Capacity;
import dev.m7wq.qshopapi.payment.PlayerPurchaseEvent;
import dev.m7wq.qshopapi.storage.Edits;
import dev.m7wq.qshopapi.storage.Inputs;
import dev.m7wq.qshopapi.utils.ShopUtil;
import dev.m7wq.qshopapi.utils.TextHelper;
import dev.m7wq.test.Main;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import lombok.SneakyThrows;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

public class ShopReader implements Reader<ShopInterface> {

    @SneakyThrows
    @Override
    public ShopInterface read(Object instance, Class<?> clazz, ShopAPI api, Plugin plugin) {

        Edits edits = api.getEdits();
        Inputs inputs = api.getInputs();

        if (!clazz.isAnnotationPresent(Shop.class))
            throw new IllegalStateException("Shop class should be annotated with @Shop annotation");

        Shop shopAnnotation = clazz.getAnnotation(Shop.class);

        if (!clazz.isAnnotationPresent(Settings.class))
            throw new IllegalStateException("You have to define settings in your shop by putting @Settings Annotation");

        Settings settings = clazz.getAnnotation(Settings.class);

        ShopSettings shopSettings = ShopSettings.builder()
                .cancelClickEvent(settings.cancelClickEvent())
                .status(settings.status()).build();

        String shopName = shopAnnotation.title();
        shopName = TextHelper.format(shopName);
        Capacity capacity = shopAnnotation.capacity();
        List<Item> items = new ArrayList<>();





        // -- FOR SLOTS --
        for (Field field : clazz.getDeclaredFields()){

            // to access it anywhere
            field.setAccessible(true);



            // @Slot(?)
            if (field.isAnnotationPresent(Slot.class)){

                if (field.getType() != Item.class)
                    throw new IllegalStateException("Singe slot datatype should be Item not "+field.getType());

                Slot slot = field.getAnnotation(Slot.class);

                int num = slot.value();

                Item item = (Item) field.get(instance);

                item.setSlot(num);

                items.add(item);

                continue;

            }

            // @Slots
            if (field.isAnnotationPresent(Slots.class)){

                if (field.getType() != Item[].class)
                    throw new IllegalStateException("Several Slots DataType should be Item[] not "+field.getType());

                Item[] value = (Item[]) field.get(instance);

                Collections.addAll(items, value);

                continue;
            }


        }

        ShopInterface shopInterface = ShopInterface.builder()
                .title(shopName)
                .capacity(capacity)
                .items(items)
                .settings(shopSettings)
                .build();

        if (shopSettings.getStatus()== ShopStatus.SELECTABLE){
            if (!api.getStorage().containsKey(shopName)){

                HashMap<Item, ItemStatus> map = new HashMap<>();

                items.forEach(item->map.put(item,ItemStatus.NOT_PURCHASED));

                api.getStorage().put(shopName, map);
            }
        }


        // -- FOR PURPOSES --
        for (Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);

            // Handle inputs because the priority for it
            if (field.isAnnotationPresent(Input.class)) {

                Input input = field.getAnnotation(Input.class);

                String key = input.value();

                dev.m7wq.qshopapi.entity.Input theInput = inputs.getInputMap().get(key);

                field.set(instance, theInput.getValue());



            }

            // Handle purposes
            if (field.isAnnotationPresent(Clickable.class)){

                Clickable clickable = field.getAnnotation(Clickable.class);

                ClickPurpose purpose = clickable.purpose();
                int slot = clickable.forSlot();

                // Handle commands purpose
                if (purpose == ClickPurpose.PERFORM_COMMAND){
                    if (!(field.get(instance) instanceof Command<?>) )
                        throw new IllegalStateException("To perform command you have to use Command<BukkitSource> object of dev.velix.imperat");

                    Command<BukkitSource> command = (Command<BukkitSource>) field.get(instance);

                    ActionBase actionBase = new ActionBase(plugin);

                    actionBase.registerCommand(command);

                    shopInterface.getItems().forEach(item ->{
                        if (item.getSlot()==slot)
                            item.setListener(new ItemListener() {
                                @Override
                                public void onClick(Player player, Item item) {
                                    Bukkit.getServer().dispatchCommand(player,command.name());
                                }
                            });
                    });


                }else if (purpose == ClickPurpose.DIRECT_PURCHASE){


                    Item item = shopInterface.getItems().stream().filter(i->i.getSlot() ==slot).findFirst().get();



                    Object sale = field.get(instance);

                    if (!(sale instanceof ItemStack))
                        new IllegalStateException("DIRECT_PURCHASE field dataType can only be ItemStack not "+field.getType());



                    item.setListener(new ItemListener() {
                        @Override
                        public void onClick(Player player, Item item) {
                            Bukkit.getPluginManager().callEvent(new PlayerPurchaseEvent(player, item.getPrice()));
                            player.getInventory().addItem((ItemStack) sale);
                        }
                    });




                }else if (purpose == ClickPurpose.OPEN_INVENTORY){

                    Item item = shopInterface.getItems().stream().filter(i->i.getSlot() ==slot).findFirst().get();


                    Object obj = field.get(instance);

                    if (!(obj instanceof Inventory))
                        new IllegalStateException("OPEN_INVENTORY field dataType can only be Inventory not "+field.getType());

                    item.setListener(new ItemListener() {
                        @Override
                        public void onClick(Player player, Item item) {
                            player.openInventory((Inventory) obj);
                        }
                    });


                }
            }



        }


        // Handle sub-shops

        List<ShopInterface> subShops = new ArrayList<>();

        for (Class<?> subClazz : clazz.getClasses()){

            if (subClazz.isAnnotationPresent(Shop.class)){
                if (subClazz.isAnnotationPresent(Clickable.class)){

                    Clickable annotation = subClazz.getAnnotation(Clickable.class);

                    ClickPurpose purpose = annotation.purpose();

                    if (purpose != ClickPurpose.OPEN_SUB_SHOP)
                        continue;

                    ShopInterface subShop = read(instance,subClazz, api, plugin);

                    subShops.add(subShop);

                    int slot = annotation.forSlot();

                    shopInterface.getItems().forEach(item -> {

                        if (item.getSlot()==slot){

                            item.setListener(new ItemListener() {
                                @Override
                                public void onClick(Player player, Item item) {
                                    player.openInventory(ShopUtil.toInventory(subShop,edits));
                                }
                            });
                        }

                    });


                }


            }



        }

        shopInterface.setSubShops(subShops);

        return shopInterface;
    }
}

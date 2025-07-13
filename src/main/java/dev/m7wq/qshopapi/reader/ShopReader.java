package dev.m7wq.qshopapi.reader;


import dev.m7wq.qshopapi.ActionBase;
import dev.m7wq.qshopapi.annotations.*;
import dev.m7wq.qshopapi.annotations.enums.ClickPurpose;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.m7wq.qshopapi.main.enums.Capacity;
import dev.m7wq.qshopapi.payment.PlayerPurchaseEvent;
import dev.m7wq.qshopapi.storage.Inputs;
import dev.m7wq.qshopapi.utils.ShopUtil;
import dev.m7wq.qshopapi.utils.TextHelper;
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

public class ShopReader implements Readable<ShopInterface> {

    @SneakyThrows
    @Override
    public ShopInterface read(Object instance,Class<?> clazz, Inputs inputs, Plugin plugin) {

        if (!clazz.isAnnotationPresent(Shop.class))
            throw new IllegalStateException("Shop class should be annotated with @Shop annotation");


        Shop shopAnnotation = clazz.getAnnotation(Shop.class);

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
                .build();


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
            if (field.isAnnotationPresent(AnnotatedClickable.class)){

                AnnotatedClickable clickable = field.getAnnotation(AnnotatedClickable.class);

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
                            item.setClickable((e)->{
                                if (e.getWhoClicked() instanceof Player player)
                                    player.performCommand(command.name());
                            });
                    });


                }else if (purpose == ClickPurpose.DIRECT_PURCHASE){


                    Item item = shopInterface.getItems().stream().filter(i->i.getSlot() ==slot).findFirst().get();



                    Object sale = field.get(instance);

                    if (!(sale instanceof ItemStack))
                        new IllegalStateException("DIRECT_PURCHASE field dataType can only be ItemStack not "+field.getType());



                    item.setClickable(e-> {
                        if (e.getWhoClicked() instanceof Player player) {
                            Bukkit.getPluginManager().callEvent(new PlayerPurchaseEvent(player, item.getPrice()));
                            player.getInventory().addItem((ItemStack) sale);
                        }

                    });




                }else if (purpose == ClickPurpose.OPEN_INVENTORY){

                    Item item = shopInterface.getItems().stream().filter(i->i.getSlot() ==slot).findFirst().get();


                    Object obj = field.get(instance);

                    if (!(obj instanceof Inventory))
                        new IllegalStateException("OPEN_INVENTORY field dataType can only be Inventory not "+field.getType());

                    item.setClickable(e->{

                        if (e.getWhoClicked() instanceof Player player){
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
                if (subClazz.isAnnotationPresent(AnnotatedClickable.class)){

                    AnnotatedClickable annotation = subClazz.getAnnotation(AnnotatedClickable.class);

                    ClickPurpose purpose = annotation.purpose();

                    if (purpose != ClickPurpose.OPEN_SUB_SHOP)
                        continue;

                    ShopInterface subShop = read(instance,subClazz, inputs, plugin);

                    subShops.add(subShop);

                    int slot = annotation.forSlot();

                    shopInterface.getItems().forEach(item -> {

                        if (item.getSlot()==slot){

                            item.setClickable(e->{
                                if (e.getWhoClicked() instanceof Player plr){
                                    plr.openInventory(ShopUtil.toInventory(subShop));
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

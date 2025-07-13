package dev.m7wq.qshopapi.entity;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class Input {

    @Getter
    private Object value;

    public static Input of(Object value){

        Command command = Command.create("lol").build();

        if (!(value instanceof Command<?>)
                && !(value instanceof Inventory)
                && !(value instanceof ItemStack)
                && !(value instanceof Item))
            throw new IllegalStateException("Not valid input: "+value.getClass().getName());

        return new Input(value);

    }
}

package dev.m7wq.qshopapi.entity;

import dev.velix.imperat.command.Command;
import lombok.Getter;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class Input<T> {

    List<Class<?>> classes = Arrays.asList(Command.class, Inventory.class);

    public Input(){}

    private Input(T value){
        this.value = value;
    }

    @Getter
    private T value;

    public Input<T> of(T input){

        if (classes.contains(input.getClass())){

            return new Input<T>(input);


        }

        throw new IllegalStateException("Not valid input: "+input.getClass().getName());

    }
}

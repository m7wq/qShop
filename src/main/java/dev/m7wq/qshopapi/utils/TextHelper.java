package dev.m7wq.qshopapi.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TextHelper {

    public String format(String str){
        return ChatColor.translateAlternateColorCodes('&',str);
    }

    public List<String> format(List<String> stringList){

        List<String> colorized = new ArrayList<>();

        for (String string : stringList){
            colorized.add(format(string));
        }

        return colorized;
    }
}

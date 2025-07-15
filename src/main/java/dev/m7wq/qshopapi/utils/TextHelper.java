package dev.m7wq.qshopapi.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TextHelper {

    public String format(String str){

        return str==null ? "" : ChatColor.translateAlternateColorCodes('&',str);
    }

    public List<String> format(List<String> stringList){

        if (stringList==null)return new ArrayList<>();

        List<String> colorized = new ArrayList<>();

        for (String string : stringList){
            colorized.add(format(string));
        }

        return colorized;
    }
}

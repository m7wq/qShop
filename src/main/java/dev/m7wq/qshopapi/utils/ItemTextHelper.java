package dev.m7wq.qshopapi.utils;

import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.storage.Edits;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class ItemTextHelper {

    public ItemTextHelper()
    {

    }

    Item item;


    String format(String string, Edits edits){

        String str = string;



        for (String key : edits.getEditable().keySet()){
            if (ChatColor.stripColor(str).contains(key))
               str = edits.getEditable().get(key).replace(str,item);
        }

        return ChatColor.translateAlternateColorCodes('&',str);
    }



    List<String> format(List<String> stringList, Edits edits){

        List<String> formated = new ArrayList<>();

        for (String str : stringList) {

            formated.add(format(str, edits));
        }



        return formated;
    }
}

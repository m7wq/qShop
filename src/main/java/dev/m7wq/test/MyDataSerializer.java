package dev.m7wq.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.m7wq.qshopapi.data.DataSerializer;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;


import java.lang.reflect.Type;
import java.util.HashMap;

public class MyDataSerializer extends DataSerializer {

    // Save it as JSON in your database or file etc...
    @Override
    public void serialize(HashMap<String, HashMap<Item, ItemStatus>> map) {

        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        // your serialize method
        // TIP: for sql datatype use TEXT not VARCHAR!!
        // TIP: for mongodb use Document.parse(jsonString)

    }

    @Override
    public HashMap<String, HashMap<Item, ItemStatus>> deserialize() {

        Gson gson = new Gson();

        String json = "deserialize json string"; // your deserialize method

        Type type = new TypeToken<HashMap<String, HashMap<Item, ItemStatus>>>(){}.getType();

        HashMap<String, HashMap<Item, ItemStatus>> map = gson.fromJson(json,type);

        return map;


    }
}

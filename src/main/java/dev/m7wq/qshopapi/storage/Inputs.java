package dev.m7wq.qshopapi.storage;



import dev.m7wq.qshopapi.entity.Input;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class Inputs {
    protected HashMap<String, Input> inputMap = new HashMap<>();

}

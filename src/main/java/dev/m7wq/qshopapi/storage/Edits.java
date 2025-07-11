package dev.m7wq.qshopapi.storage;

import dev.m7wq.qshopapi.extra.Editable;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Edits {
    protected Map<String, Editable> editable = new HashMap<>();
}

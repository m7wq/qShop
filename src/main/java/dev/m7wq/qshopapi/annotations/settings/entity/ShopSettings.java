package dev.m7wq.qshopapi.annotations.settings.entity;

import dev.m7wq.qshopapi.annotations.settings.enums.ShopStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShopSettings {
    ShopStatus status;
    boolean cancelClickEvent;
}

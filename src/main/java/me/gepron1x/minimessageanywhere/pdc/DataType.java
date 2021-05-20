package me.gepron1x.minimessageanywhere.pdc;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public final class DataType {
    private DataType() {}
    public static final ComponentDataType COMPONENT = new ComponentDataType();
    public static final ComponentCollectionDataType<List<Component>> COMPONENT_LIST = new ComponentCollectionDataType<>(ArrayList::new);
}

package me.gepron1x.minimessageanywhere.pdc;

import me.gepron1x.minimessageanywhere.util.PacketItemData;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public final class DataType {
    private DataType() {}
    public static final ComponentDataType COMPONENT = new ComponentDataType();
    public static final ComponentCollectionDataType<List<Component>> COMPONENT_LIST = new ComponentCollectionDataType<>(ArrayList::new);
    public static final PacketItemDataType ITEM_DATA = new PacketItemDataType();
    public static final PacketBookDataType BOOK_DATA = new PacketBookDataType();

}

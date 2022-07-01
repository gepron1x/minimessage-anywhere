package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.packetlistener.out.item.ConvertedItemStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ItemListener extends AbstractListener {

    private static final ComponentHandler DISABLE_ITALIC = (audience, component) ->
            Component.text().decoration(TextDecoration.ITALIC, false).append(component).build();

    private final ComponentHandler itemHandler;




    public ItemListener(Plugin plugin, ComponentHandler handler, boolean disableItalic) {
        super(plugin,
                handler,
                PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Client.SET_CREATIVE_SLOT
        );
        this.itemHandler = disableItalic ? handler.andThen(DISABLE_ITALIC) : handler;
    }

    
    private void processItem(Audience audience, @Nullable ItemStack itemStack) {
        if (itemStack == null) return;
        new ConvertedItemStack(this.itemHandler, audience, itemStack).convert();
    }

    private List<Component> processList(Audience audience, List<Component> components) {
        List<Component> newList = new ArrayList<>(components.size());
        for(Component component : components) {
            newList.add(handler.handle(audience, component));
        }
        return newList;
    }


    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack itemStack = packet.getItemModifier().read(0);
        if (itemStack == null) return;
        new ConvertedItemStack(itemHandler, event.getPlayer(), itemStack).revert();
        packet.getItemModifier().write(0, itemStack);
        event.setPacket(packet);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType type = event.getPacketType();
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        if(type == PacketType.Play.Server.SET_SLOT) {
            ItemStack item = packet.getItemModifier().read(0);
            processItem(player, item);
            packet.getItemModifier().write(0, item);
        } else if(type == PacketType.Play.Server.WINDOW_ITEMS) {
            List<ItemStack> items = packet.getItemListModifier().read(0);
            for(ItemStack item : items) {
                processItem(player, item);
            }
            packet.getItemListModifier().write(0, items);
        }
        event.setPacket(packet);
    }
}

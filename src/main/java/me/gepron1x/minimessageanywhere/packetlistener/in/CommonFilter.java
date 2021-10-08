package me.gepron1x.minimessageanywhere.packetlistener.in;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import me.gepron1x.minimessageanywhere.util.MiniMessageTokenStripper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;

public class CommonFilter extends AbstractFilter {


    public CommonFilter(Plugin plugin, Predicate<Player> ignore, MiniMessageTokenStripper stripper) {
        super(plugin, ignore, stripper,
                PacketType.Play.Client.CHAT, PacketType.Play.Client.ITEM_NAME, PacketType.Play.Client.CLIENT_COMMAND);
    }

    @Override
    public void process(PacketEvent event) {
        PacketContainer container = event.getPacket();
        StructureModifier<String> strings = container.getStrings();
        for (int i = 0; i < strings.size(); i++) {
            String value = strings.read(i);
            if (value == null) continue;
            String result = stripper.strip(value);
            strings.write(i, result);
        }
        event.setPacket(container);
    }


}

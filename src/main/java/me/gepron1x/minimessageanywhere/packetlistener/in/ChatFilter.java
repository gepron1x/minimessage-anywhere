package me.gepron1x.minimessageanywhere.packetlistener.in;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ChatFilter extends PacketAdapter {
    private final UnaryOperator<String> filter;
    private final Predicate<Player> ignore;

    public ChatFilter(Plugin plugin, UnaryOperator<String> filter, Predicate<Player> ignore) {
        super(plugin, PacketType.Play.Client.CHAT);
        this.filter = filter;
        this.ignore = ignore;

    }
    public ChatFilter(Plugin plugin, UnaryOperator<String> filter) {
        this(plugin, filter, player -> false);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(ignore.test(event.getPlayer())) return;

        PacketContainer packet = event.getPacket();
        StructureModifier<String> strings = packet.getStrings();
        strings.write(0, filter.apply(strings.read(0)));
        event.setPacket(packet);

    }

    @Override
    public void onPacketSending(PacketEvent event) {}
}

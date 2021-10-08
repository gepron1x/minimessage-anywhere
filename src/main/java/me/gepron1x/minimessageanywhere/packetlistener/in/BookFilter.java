package me.gepron1x.minimessageanywhere.packetlistener.in;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Preconditions;
import me.gepron1x.minimessageanywhere.util.MiniMessageTokenStripper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BookFilter extends AbstractFilter {

    public BookFilter(Plugin plugin, Predicate<Player> ignore, MiniMessageTokenStripper stripper) {
        super(plugin, ignore, stripper, PacketType.Play.Client.B_EDIT);
    }

    @Override
    public void process(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        @SuppressWarnings("unchecked") List<String> pages = packet.getSpecificModifier(List.class).read(0);
        List<String> newPages = new ArrayList<>(pages.size());
        for (String page : pages) newPages.add(stripper.strip(page));

        Optional<?> opt = packet.getSpecificModifier(Optional.class).read(0);
        opt.ifPresent(obj -> Preconditions.checkState(obj instanceof String, "optional does not contain a string"));


        packet.getModifier().write(2, opt.map(String.class::cast).map(stripper::strip));

        packet.getSpecificModifier(List.class).write(0, newPages);
        event.setPacket(packet);

    }
}

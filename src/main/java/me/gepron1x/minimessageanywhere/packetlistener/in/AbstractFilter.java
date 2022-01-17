package me.gepron1x.minimessageanywhere.packetlistener.in;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.util.MiniMessageEscaper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;

public abstract class AbstractFilter extends PacketAdapter {
    private final Predicate<Player> ignore;
    protected final MiniMessageEscaper escape;

    protected AbstractFilter(Plugin plugin, Predicate<Player> ignore, MiniMessageEscaper escape, PacketType... types) {
        super(plugin, types);
        this.ignore = ignore;
        this.escape = escape;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (ignore.test(event.getPlayer())) return;
        process(event);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
    }

    public abstract void process(PacketEvent event);

}

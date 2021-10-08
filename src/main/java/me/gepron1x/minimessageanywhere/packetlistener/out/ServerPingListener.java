package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import org.bukkit.plugin.Plugin;

public class ServerPingListener extends AbstractListener {
    public ServerPingListener(Plugin plugin, ComponentHandler handler) {
        super(plugin, handler, PacketType.Status.Server.SERVER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrappedServerPing ping = packet.getServerPings().read(0);
        ping.setMotD(handler.handle(event.getPlayer(), ping.getMotD()));
        packet.getServerPings().write(0, ping);
        event.setPacket(packet);
    }
}

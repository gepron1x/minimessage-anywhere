package me.gepron1x.minimessageanywhere.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

public class ServerPingListener extends AbstractListener {
    public ServerPingListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrappedServerPing ping = packet.getServerPings().read(0);
        ping.setMotD(componentProcessor.handle(ping.getMotD()));
        packet.getServerPings().write(0, ping);
    }
}

package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;


public class PlayerInfoListener extends AbstractListener {
    public PlayerInfoListener(Plugin plugin, ComponentHandler handler) {
        super(plugin, handler, PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        List<PlayerInfoData> playerInfoDataList = packet.getPlayerInfoDataLists().read(0);
        List<PlayerInfoData> list = new ArrayList<>(playerInfoDataList.size());
        for (PlayerInfoData pid : playerInfoDataList) {

            PlayerInfoData playerInfoData = new PlayerInfoData(
                    pid.getProfile(),
                    pid.getLatency(),
                    pid.getGameMode(),
                    handler.handleIfNotNull(event.getPlayer(), pid.getDisplayName())
            );
            list.add(playerInfoData);
        }
        packet.getPlayerInfoDataLists().write(0, list);
        event.setPacket(packet);

    }
}

package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerInfoListener extends AbstractListener {
    public PlayerInfoListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        List<PlayerInfoData> playerInfoDataList = packet.getPlayerInfoDataLists().read(0);
        packet.getPlayerInfoDataLists().write(0, playerInfoDataList.stream().map(pid ->
                new PlayerInfoData(
                        pid.getProfile(),
                        pid.getLatency(),
                        pid.getGameMode(),
                        pid.getDisplayName() != null ? componentProcessor.handle(pid.getDisplayName()) : null
                )
        ).collect(Collectors.toList()));
        event.setPacket(packet);

    }
}

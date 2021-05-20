package me.gepron1x.minimessageanywhere.processors;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.collect.Maps;
import me.gepron1x.minimessageanywhere.wrapper.WrapperPlayServerAdvancements;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Map;

;;

public class AdvancementListener extends AbstractListener {

    public AdvancementListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.ADVANCEMENTS);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperPlayServerAdvancements advancementsPacket = new WrapperPlayServerAdvancements(event.getPacket());
        advancementsPacket.getAdvancements().ifPresent(map -> {
            final Map<MinecraftKey, WrapperPlayServerAdvancements.SerializedAdvancement> newMap = Maps.newHashMap();
            map.forEach((key, advancement) -> {
                WrapperPlayServerAdvancements.AdvancementDisplay display = advancement.display;
                display.title = componentProcessor.handle(display.title);
                display.description = componentProcessor.handle(display.description);
                newMap.put(key, advancement);
            });
            advancementsPacket.setAdvancements(newMap);
        });
        event.setPacket(advancementsPacket.getHandle());
    }
}

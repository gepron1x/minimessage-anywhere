package me.gepron1x.minimessageanywhere.packetlistener.out;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.ComponentHandler;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.wrapper.WrapperPlayServerMap;

import static me.gepron1x.minimessageanywhere.wrapper.WrapperPlayServerMap.*;


public class MapListener extends AbstractListener {

    public MapListener(MiniMessageAnywhere plugin, ComponentHandler handler) {
        super(plugin, handler, PacketType.Play.Server.MAP);
    }


    @Override
    public void onPacketSending(final PacketEvent event) {
        WrapperPlayServerMap mapPacket = new WrapperPlayServerMap(event.getPacket());
        MapIcon[] icons = mapPacket.getMapIcons();
        for(MapIcon icon : icons) {
            icon.name = handler.handleIfNotNull(event.getPlayer(), icon.name);
        }
        mapPacket.setMapIcons(icons);
        event.setPacket(mapPacket.getHandle());
    }
}

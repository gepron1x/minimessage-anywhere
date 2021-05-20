package me.gepron1x.minimessageanywhere.processors;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Collection;


public class CommonListener extends AbstractListener {


    public CommonListener(MiniMessageAnywhere plugin, Collection<PacketType> types) {
        super(plugin, types);

    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packetContainer = event.getPacket();
        StructureModifier<WrappedChatComponent> chatComponentStructureModifier = packetContainer.getChatComponents();
        for(int i = 0; i < chatComponentStructureModifier.size(); i++) {
            WrappedChatComponent wrappedComponent = chatComponentStructureModifier.readSafely(i);
            if(wrappedComponent == null) continue;
            chatComponentStructureModifier.write(i, componentProcessor.handle(wrappedComponent));
        }
        event.setPacket(packetContainer);
    }
}

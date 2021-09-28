package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.UnaryOperator;


public class CommonListener extends AbstractListener {

    public CommonListener(MiniMessageAnywhere plugin, ComponentHandler handler, Collection<PacketType> types) {
        super(plugin, handler, types);

    }


    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packetContainer = event.getPacket();
        final Player player = event.getPlayer();
        replace(packetContainer.getChatComponents(), c -> handler.handle(player, c));
        replace(packetContainer.getSpecificModifier(Component.class), c -> handler.handle(player, c));
        replace(packetContainer.getChatComponentArrays(), c -> adapt(player, c));
        event.setPacket(packetContainer);
    }
    private WrappedChatComponent[] adapt(Audience audience, WrappedChatComponent[] input) {
        for(int i = 0; i < input.length; i++) input[i] = handler.handle(audience, input[i]);
        return input;

    }

    private <T> void replace(StructureModifier<T> modifier, UnaryOperator<T> replacer) {
        for(int i = 0; i < modifier.size(); i++) {
            T t = modifier.readSafely(i);
            if (t == null) continue;
            modifier.write(i, replacer.apply(t));
        }
    }




}

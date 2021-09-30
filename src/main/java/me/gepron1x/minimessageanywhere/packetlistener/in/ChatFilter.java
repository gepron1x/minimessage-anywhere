package me.gepron1x.minimessageanywhere.packetlistener.in;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFilter extends PacketAdapter {
    private static final Pattern TAG = Pattern.compile("(</?([#:<>a-zA-Z\\d]+)>)");
    private final Predicate<Player> ignore;
    private final String replacement;
    private final String[] tokens;

    public ChatFilter(Plugin plugin, String[] tokens, String replacement, Predicate<Player> ignore) {
        super(plugin, PacketType.Play.Client.CHAT);
        this.tokens = tokens;
        this.ignore = ignore;
        this.replacement = replacement;
    }
    public ChatFilter(Plugin plugin, String[] tokens, String replacement) {
        this(plugin, tokens, replacement, player -> false);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        if(ignore.test(event.getPlayer())) return;

        PacketContainer packet = event.getPacket();
        StructureModifier<String> strings = packet.getStrings();
        String message = strings.read(0);
        Matcher matcher = TAG.matcher(message);
        while(matcher.find()) {
            String target = matcher.group(1);
            String value = matcher.group(2).toLowerCase();
            for (String token : tokens) {
                if (value.startsWith(token)) message = StringUtils.replace(message, target, replacement);
            }
        }
        strings.write(0, message);
        event.setPacket(packet);

    }

    @Override
    public void onPacketSending(PacketEvent event) {}
}

package me.gepron1x.minimessageanywhere;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import me.gepron1x.minimessageanywhere.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.stream.Collectors;

public final class MiniMessageAnywhere extends JavaPlugin {
    private final ComponentProcessor componentProcessor = new ComponentProcessor();
    private ProtocolManager protocolManager;


    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        saveDefaultConfig();
        reload();

        getCommand("mmanywhere").setExecutor(((sender, command, label, args) -> {
            if(args.length == 0) {
                sender.sendMessage("mmanywhere v1.0.0");
            }
            if(args[0].equals("reload") && sender.hasPermission("mmanywhere.reload")) {
                reload();
                sender.sendMessage("[mmanywhere] reload successfully");
            }
            return true;
        }));
        getLogger().info("Plugin successfully enabled.");



    }
    public void reload() {
        protocolManager.removePacketListeners(this);
        reloadConfig();
        Collection<PacketType> packetTypes = getConfig()
                .getStringList("packets-to-listen")
                .stream().flatMap(s -> PacketType.fromName(s).stream()).collect(Collectors.toSet());
        protocolManager.addPacketListener(new CommonListener(this, packetTypes));
        if(getConfig().getBoolean("items-enabled"))
            protocolManager.addPacketListener(new ItemListener(this));
        if(getConfig().getBoolean("advancements-enabled"))
            protocolManager.addPacketListener(new AdvancementListener(this));
        if(getConfig().getBoolean("entity-nametags-enabled"))
            protocolManager.addPacketListener(new EntityMetadataListener(this));
        if(getConfig().getBoolean("map-icons-enabled"))
            protocolManager.addPacketListener(new MapListener(this));
        if(getConfig().getBoolean("player-info-enabled"))
            protocolManager.addPacketListener(new PlayerInfoListener(this));
        if(getConfig().getBoolean("server-motd-enabled"))
            protocolManager.addPacketListener(new ServerPingListener(this));


    }

    public ComponentProcessor getComponentProcessor() {
        return componentProcessor;
    }

    @Override
    public void onDisable() {
       protocolManager.removePacketListeners(this);
       getLogger().info("Plugin disabled, cya!");

    }
}

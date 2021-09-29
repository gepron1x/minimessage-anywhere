package me.gepron1x.minimessageanywhere;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.exceptions.NoSuchCommandException;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.gepron1x.minimessageanywhere.config.Config;
import me.gepron1x.minimessageanywhere.config.ConfigManager;
import me.gepron1x.minimessageanywhere.config.ParsingStrategy;
import me.gepron1x.minimessageanywhere.config.serializer.MessageSerializer;
import me.gepron1x.minimessageanywhere.config.serializer.PacketTypeSerializer;
import me.gepron1x.minimessageanywhere.handler.GlobalComponentHandler;
import me.gepron1x.minimessageanywhere.handler.MiniMessageComponentHandler;
import me.gepron1x.minimessageanywhere.listener.PrettyChatListener;
import me.gepron1x.minimessageanywhere.packetlistener.in.ChatFilter;
import me.gepron1x.minimessageanywhere.packetlistener.out.*;
import me.gepron1x.minimessageanywhere.processor.MiniMessageProcessor;
import me.gepron1x.minimessageanywhere.util.RegexUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Tokens;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;



public final class MiniMessageAnywhere extends JavaPlugin {
    private static final String NAME = "MiniMessageAnywhere";


    private static final String[] MINI_MESSAGE_TOKENS = new String[]{
            Tokens.CLICK, Tokens.HOVER, Tokens.KEYBIND,
            Tokens.TRANSLATABLE, Tokens.TRANSLATABLE_2, Tokens.TRANSLATABLE_3,
            Tokens.INSERTION, Tokens.COLOR, Tokens.COLOR_2,
            Tokens.COLOR_3, Tokens.HEX, Tokens.FONT,
            Tokens.UNDERLINED, Tokens.UNDERLINED_2, Tokens.STRIKETHROUGH,
            Tokens.STRIKETHROUGH_2, Tokens.OBFUSCATED, Tokens.OBFUSCATED_2,
            Tokens.ITALIC, Tokens.ITALIC_2, Tokens.ITALIC_3,
            Tokens.BOLD, Tokens.BOLD_2, Tokens.RESET,
            Tokens.RESET_2, Tokens.PRE, Tokens.GRADIENT
    };


    private ConfigManager<Config> configManager;
    private final MiniMessage miniMessage = MiniMessage.get();
    private PaperCommandManager<CommandSender> commandManager;
    private GlobalComponentHandler handler;
    private MiniMessageComponentHandler miniComponentHandler;
    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        handler = new GlobalComponentHandler();
        ConfigurationOptions options = new ConfigurationOptions.Builder()
                .addSerialiser(new PacketTypeSerializer())
                .addSerialiser(new MessageSerializer(miniMessage))
                .sorter(new AnnotationBasedSorter())
                .build();
        configManager = ConfigManager.create(getDataFolder().toPath(), "config.yml", Config.class, options);
        enable();



        getLogger().info("Plugin successfully enabled.");



    }

    /**
     * reloads a plugin
     */
    public void reload() {
        disable();
        enable();

    }
    private void enable() {
        configManager.reloadConfig();
        Config config = configManager.getConfigData();
        MiniMessageProcessor processor = setupProcessor();
        miniComponentHandler = new MiniMessageComponentHandler(miniMessage, processor);
        handler.addHandler(miniComponentHandler);
        registerListeners();

        setupCommand();


        if(config.prettyChat()) {
            getServer().getPluginManager().registerEvents(new PrettyChatListener(getServer(), handler), this);
        }

        Config.Filter filterConfig = config.filter();
        if(filterConfig.enabled()) {
            protocolManager.addPacketListener(new ChatFilter(
                    this,
                    MINI_MESSAGE_TOKENS,
                    filterConfig.replacement(),
                    player -> player.hasPermission(Permissions.IGNORE_FILTER)
            ));
        }



    }
    private MiniMessageProcessor setupProcessor() {
        Config config = configManager.getConfigData();
        MiniMessageProcessor processor;
        if(config.parsingStrategy() == ParsingStrategy.ALL) {
            processor = MiniMessageProcessor.all();
        } else {
            Config.Regex regexConfig = config.regex();
            String prefix = RegexUtils.adaptUserInput(regexConfig.prefix());
            String suffix = RegexUtils.adaptUserInput(regexConfig.suffix());
            Pattern pattern = Pattern.compile(MessageFormat.format("{0}(.+){1}", prefix, suffix));
            processor = MiniMessageProcessor.regex(pattern);
        }
        return processor;
    }
    private void registerListeners() {
        Config config = configManager.getConfigData();


        Set<AbstractListener> listeners = new HashSet<>();

        listeners.add(new CommonListener(this, handler, config.packetsToListen()));

        Config.Specific specific = config.specific();
        if(specific.items()){
            listeners.add(new ItemListener(this, handler));
        }
        if(specific.entities()) {
            listeners.add(new EntityMetadataListener(this, handler));
        }
        if(specific.mapIcons()) {
            listeners.add(new MapListener(this, handler));
        }
        if(specific.playerInfo()) {
            listeners.add(new PlayerInfoListener(this, handler));
        }
        if(specific.MoTD()) {
            listeners.add(new ServerPingListener(this, handler));
        }
        listeners.forEach(protocolManager::addPacketListener);
    }
    private void setupCommand() {
        try {
             commandManager = new PaperCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
        } catch (Exception e) {
            getLogger().severe("failed to initialize CommandManager. please contact developer");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (commandManager.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            commandManager.registerBrigadier();
        }
        Command.Builder<CommandSender> builder = commandManager.commandBuilder("mmanywhere");

        commandManager.command(builder
                .permission(Permissions.INFO)
                .handler(ctx ->
                        ctx.getSender().sendMessage(configManager.getConfigData().messages().info())
                ));
        commandManager.command(builder.literal("reload")
                .permission(Permissions.RELOAD)
                .handler(ctx -> {
                    reload();
                    ctx.getSender().sendMessage(configManager.getConfigData().messages().reloaded());
                })
        );
        commandManager.registerExceptionHandler(NoPermissionException.class,
                (sender, e) -> sender.sendMessage(configManager.getConfigData().messages().noPermission())
        );
        commandManager.registerExceptionHandler(NoSuchCommandException.class,
                (sender, e) -> sender.sendMessage(configManager.getConfigData().messages().unknownCommand())
        );
        commandManager.registerExceptionHandler(InvalidSyntaxException.class,
                (sender, e) -> sender.sendMessage(configManager.getConfigData().messages().unknownCommand())
        );

    }


    private void disable() {
        handler.removeHandler(miniComponentHandler);
        HandlerList.unregisterAll(this);
        protocolManager.removePacketListeners(this);

    }




    /**
     * returns a GlobalComponentHandler, where you can add your own handlers
     * @return handler
     */
    @NotNull
    public GlobalComponentHandler getHandler() {
        return handler;
    }

    /**
     * a shortcut to easly get plugin from PluginManager
     * @param manager - manager to get plugin from
     * @return a minimessage anywhere plugin.
     */
    @NotNull
    public static MiniMessageAnywhere get(PluginManager manager) {
        return (MiniMessageAnywhere)
                Objects.requireNonNull(manager.getPlugin(NAME), "MiniMessageAnywhere wasnt loaded!");
    }

    @Override
    public void onDisable() {
        disable();
        getLogger().info("Plugin disabled, cya!");

    }
}

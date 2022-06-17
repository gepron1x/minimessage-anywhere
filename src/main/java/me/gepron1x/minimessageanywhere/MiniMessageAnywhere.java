package me.gepron1x.minimessageanywhere;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.exceptions.NoSuchCommandException;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.comphenix.protocol.PacketType;
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
import me.gepron1x.minimessageanywhere.packetlistener.in.BookFilter;
import me.gepron1x.minimessageanywhere.packetlistener.in.CommonFilter;
import me.gepron1x.minimessageanywhere.packetlistener.out.*;
import me.gepron1x.minimessageanywhere.processor.MiniMessageProcessor;
import me.gepron1x.minimessageanywhere.util.MiniMessageEscaper;
import me.gepron1x.minimessageanywhere.util.RegexUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;



public final class MiniMessageAnywhere extends JavaPlugin {
    private static final String NAME = "MiniMessageAnywhere";
    private static final Pattern EMPTY = Pattern.compile("((.+))");

    private ConfigManager<Config> configManager;
    private MiniMessage miniMessage;
    private PaperCommandManager<CommandSender> commandManager;
    private GlobalComponentHandler handler;
    private MiniMessageComponentHandler miniComponentHandler;
    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        handler = new GlobalComponentHandler();
        ConfigurationOptions options = new ConfigurationOptions.Builder()
                .addSerialiser(new PacketTypeSerializer(PacketType.Play.Server.getInstance()))
                .addSerialiser(new MessageSerializer(MiniMessage.miniMessage()))
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
        miniMessage = MiniMessage.builder().tags(
                        TagResolver.resolver(
                                config.miniMessageSettings().placeholderResolver(),
                                config.miniMessageSettings().transformationRegistry()
                        )
                )
                .build();
        Pattern messagePattern = getMessagePattern();
        MiniMessageProcessor processor = setupProcessor(messagePattern);
        miniComponentHandler = new MiniMessageComponentHandler(miniMessage, processor);
        handler.addHandler(miniComponentHandler);
        registerListeners();

        setupCommand();


        if (config.prettyChat()) {
            getServer().getPluginManager().registerEvents(new PrettyChatListener(getServer(), handler), this);
        }

        Config.Filter filterConfig = config.filter();

        List<PacketType> commonFilters = filterConfig.common();
        Predicate<Player> ignore = player -> player.hasPermission(Permissions.IGNORE_FILTER);
        MiniMessageEscaper stripper = new MiniMessageEscaper(miniMessage);
        if (!commonFilters.isEmpty()) {
            protocolManager.addPacketListener(new CommonFilter(this, ignore, stripper));
        }
        if (filterConfig.books()) {
            protocolManager.addPacketListener(new BookFilter(this, ignore, stripper));
        }


    }

    private MiniMessageProcessor setupProcessor(Pattern messagePattern) {
        if (messagePattern == EMPTY) return MiniMessageProcessor.all();
        return MiniMessageProcessor.regex(messagePattern);
    }

    private Pattern getMessagePattern() {
        Config config = configManager.getConfigData();
        if (config.parsingStrategy() == ParsingStrategy.ALL) {
            return EMPTY;
        } else {
            Config.Regex regexConfig = config.regex();
            String prefix = RegexUtils.adaptUserInput(regexConfig.prefix());
            String suffix = RegexUtils.adaptUserInput(regexConfig.suffix());
            return Pattern.compile(MessageFormat.format("({0}(.+){1})", prefix, suffix));
        }
    }
    private void registerListeners() {
        Config config = configManager.getConfigData();


        Set<AbstractListener> listeners = new HashSet<>();

        listeners.add(new CommonListener(this, handler, config.commonPacketTypes()));

        Config.ListenTo listenTo = config.listenTo();
        Config.ListenTo.Items items = listenTo.items();

        if (items.enabled()) {
            listeners.add(new ItemListener(this, handler, items.disableItalic()));
        }
        if (listenTo.entities()) {
            listeners.add(new EntityMetadataListener(this, handler));
        }
        if (listenTo.playerInfo()) {
            listeners.add(new PlayerInfoListener(this, handler));
        }
        if (listenTo.MoTD()) {
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

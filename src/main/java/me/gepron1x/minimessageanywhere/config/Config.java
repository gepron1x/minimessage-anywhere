package me.gepron1x.minimessageanywhere.config;

import com.comphenix.protocol.PacketType;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import me.gepron1x.minimessageanywhere.util.Message;
import me.gepron1x.minimessageanywhere.util.Versions;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.text.minimessage.placeholder.Replacement;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static space.arim.dazzleconf.annote.ConfDefault.*;
import static space.arim.dazzleconf.sorter.AnnotationBasedSorter.Order;

public interface Config {
    ImmutableList<PacketType> CAVES_AND_CLIFFS_TITLE = ImmutableList.of(
            PacketType.Play.Server.SET_TITLE_TEXT, PacketType.Play.Server.SET_SUBTITLE_TEXT,
            PacketType.Play.Server.SET_ACTION_BAR_TEXT
    );
    @SuppressWarnings("deprecation")
    ImmutableList<PacketType> LEGACY_TITLE = ImmutableList.of(PacketType.Play.Server.TITLE);


    @Order(2)
    @ConfComments("What packets should we handle?")
    @SubSection Config.ListenTo listenTo();

    default List<PacketType> commonPacketTypes() {
        ImmutableList.Builder<PacketType> builder = ImmutableList.builder();
        ListenTo listenTo = listenTo();

        if (listenTo.kickAndDisconnect()) builder.add(PacketType.Play.Server.KICK_DISCONNECT);

        if (listenTo.bossBar()) builder.add(PacketType.Play.Server.BOSS);

        if (listenTo.chat()) builder.add(PacketType.Play.Server.CHAT);

        if (listenTo.inventoryTitles()) builder.add(PacketType.Play.Server.OPEN_WINDOW);

        if (listenTo.scoreboard())
            builder.add(PacketType.Play.Server.SCOREBOARD_TEAM, PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

        if (listenTo.tab()) builder.add(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        if (listenTo.titles()) builder.addAll(Versions.isCavesAndCliffs() ? CAVES_AND_CLIFFS_TITLE : LEGACY_TITLE);

        return builder.build();

    }

    interface ListenTo {
        @ConfComments("Should we handle items and books?")
        @ConfKey("items")
        @SubSection Items items();

        interface Items {
            @ConfKey("enabled")
            @DefaultBoolean(true)
            boolean enabled();

            @ConfComments({
                    "Disables italic in name and lore of the item."})
            @ConfKey("disable-italic")
            @DefaultBoolean(false)
            boolean disableItalic();

        }

        @ConfComments("Should we handle kick/disconnect messages?")
        @ConfKey("kick-disconnect")
        @DefaultBoolean(true)
        boolean kickAndDisconnect();

        @ConfComments("Should we handle bossbars?")
        @ConfKey("bossbar")
        @DefaultBoolean(true)
        boolean bossBar();

        @ConfComments("Should we handle chat messages?")
        @ConfKey("chat")
        @DefaultBoolean(true)
        boolean chat();

        @ConfComments("Should we handle inventory titles?")
        @ConfKey("inventory-titles")
        @DefaultBoolean(true)
        boolean inventoryTitles();

        @ConfComments("Should we handle scoreboard teams?")
        @ConfKey("scoreboard")
        @DefaultBoolean(true)
        boolean scoreboard();

        @ConfComments("Should we handle tab?")
        @ConfKey("tab")
        @DefaultBoolean(true)
        boolean tab();

        @ConfComments("Should we handle entity nametags?")
        @ConfKey("entities")
        @DefaultBoolean(true)
        boolean entities();

        @ConfComments("Should we handle player names?")
        @ConfKey("player-info")
        @DefaultBoolean(true)
        boolean playerInfo();

        @ConfComments("Shoudl we handle MoTD?")
        @ConfKey("motd")
        @DefaultBoolean(true)
        boolean MoTD();

        @ConfComments("Should we handle titles? (/title)")
        @ConfKey("titles")
        @DefaultBoolean(true)
        boolean titles();
    }

    @Order(3)
    @ConfComments({
            "The parsing strategy. Possible values: ALL, REGEX",
            "ALL - All messages, without regex check are going to be handled.",
            "REGEX - Only prefixed & suffixed messages will be handled.",
            "For example: [mm]Hello <red> world[/mm] will work."})
    @ConfKey("parsing-strategy")
    @DefaultString("REGEX")
    ParsingStrategy parsingStrategy();

    @Order(4)
    @ConfComments("REGEX strategy options.")
    @SubSection Regex regex();
    interface Regex {
        @ConfComments("Prefix.")
        @DefaultString("[mm]")
        String prefix();

        @ConfComments("Suffix.")
        @DefaultString("[/mm]")
        String suffix();
    }

    @Order(5)
    @ConfComments("Chat filter. Players without mmanywhere.ignore wouldnt be able to use minimessage.")
    @ConfKey("filter-chat")
    @SubSection Filter filter();

    interface Filter {

        @ConfComments("Should we filter books?")
        @ConfKey("books")
        @DefaultBoolean(true)
        boolean books();

        @ConfComments("Should we filter chat?")
        @ConfKey("chat")
        @DefaultBoolean(true)
        boolean chat();

        @ConfComments("Should we filter items renamed in anvil?")
        @ConfKey("anvil")
        @DefaultBoolean(true)
        boolean anvil();

        default List<PacketType> common() {
            ImmutableList.Builder<PacketType> builder = ImmutableList.builder();
            if (chat()) builder.add(PacketType.Play.Client.CHAT);
            if (anvil()) builder.add(PacketType.Play.Client.ITEM_NAME);
            return builder.build();
        }


    }

    @Order(6)
    @ConfComments("Should we parse messages using paper events? It is going to look fancy in console.")
    @ConfKey("pretty-chat")
    @DefaultBoolean(true)
    boolean prettyChat();

    @Order(7)
    @ConfKey("minimessage-settings")
    @SubSection MiniMessageSettings miniMessageSettings();

    interface MiniMessageSettings {

        ImmutableBiMap<String, TransformationType<?>> TRANSFORMATIONS =
                new ImmutableBiMap.Builder<String, TransformationType<?>>()
                        .put("COLOR", TransformationType.COLOR)
                        .put("DECORATION", TransformationType.DECORATION)
                        .put("HOVER_EVENT", TransformationType.HOVER_EVENT)
                        .put("CLICK_EVENT", TransformationType.CLICK_EVENT)
                        .put("KEYBIND", TransformationType.KEYBIND)
                        .put("TRANSLATABLE", TransformationType.TRANSLATABLE)
                        .put("INSERTION", TransformationType.INSERTION)
                        .put("FONT", TransformationType.FONT)
                        .put("GRADIENT", TransformationType.GRADIENT)
                        .put("RAINBOW", TransformationType.RAINBOW)
                        .build();

        @ConfComments("Global placeholders.")
        @ConfKey("placeholders")
        @DefaultMap({"hello", "<red>world"})
        Map<String, String> placeholders();

        @ConfComments({"Allowed transformations. Possible values:",
                "COLOR, DECORATION, HOVER_EVENT, CLICK_EVENT, KEYBIND",
                "TRANSLATABLE, INSERTION, FONT, GRADIENT, RAINBOW.", "" +
                "if you want to include all transformations, just use *."})
        @DefaultStrings({"*"})
        @ConfKey("transformations")
        List<String> transformations();

        default TransformationRegistry transformationRegistry() {
            List<String> transformations = transformations();
            if (transformations.size() == 1 && transformations.get(0).equals("*"))
                return TransformationRegistry.standard();
            TransformationRegistry.Builder builder = TransformationRegistry.builder().clear();
            for (String s : transformations) {
                builder.add(Objects.requireNonNull(TRANSFORMATIONS.get(s), () -> "Unknown transformation " + s));
            }
            return builder.build();
        }

        default PlaceholderResolver placeholderResolver() {
            Map<String, String> placeholders = placeholders();
            Map<String, Replacement<?>> map = new HashMap<>(placeholders.size());
            placeholders.forEach((key, value) -> map.put(key, Replacement.miniMessage(value)));
            return PlaceholderResolver.map(map);
        }


    }


    @Order(8)
    @ConfKey("messages")
    @SubSection Messages messages();

    interface Messages {

        @ConfKey("info")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> v 2.0")
        Message info();

        @ConfKey("no-permission")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> <#e60000>You dont have a permission to do this!")
        Message noPermission();

        @ConfKey("reloaded")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Plugin reloaded successfully!")
        Message reloaded();

        @ConfKey("unknown-command")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Unknown command!")
        Message unknownCommand();

    }



}

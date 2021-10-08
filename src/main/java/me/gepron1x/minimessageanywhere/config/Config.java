package me.gepron1x.minimessageanywhere.config;

import com.comphenix.protocol.PacketType;
import com.google.common.collect.ImmutableList;
import me.gepron1x.minimessageanywhere.util.Message;
import me.gepron1x.minimessageanywhere.util.Versions;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import java.util.List;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultBoolean;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;
import static space.arim.dazzleconf.sorter.AnnotationBasedSorter.Order;

public interface Config {
    ImmutableList<PacketType> CAVES_AND_CLIFFS_TITLE = ImmutableList.of(
            PacketType.Play.Server.SET_TITLE_TEXT, PacketType.Play.Server.SET_SUBTITLE_TEXT,
            PacketType.Play.Server.SET_ACTION_BAR_TEXT
    );
    @SuppressWarnings("deprecation")
    ImmutableList<PacketType> LEGACY_TITLE = ImmutableList.of(PacketType.Play.Server.TITLE);


    @Order(2)
    @ConfComments("специфичные пакеты, требующие индивидуальной обработки.")
    @SubSection Specific specific();

    default List<PacketType> commonPacketTypes() {
        ImmutableList.Builder<PacketType> builder = ImmutableList.builder();
        Specific specific = specific();

        if (specific.kickAndDisconnect()) builder.add(PacketType.Play.Server.KICK_DISCONNECT);

        if (specific.bossBar()) builder.add(PacketType.Play.Server.BOSS);

        if (specific.chat()) builder.add(PacketType.Play.Server.CHAT);

        if (specific.inventoryTitles()) builder.add(PacketType.Play.Server.OPEN_WINDOW);

        if (specific.scoreboard())
            builder.add(PacketType.Play.Server.SCOREBOARD_TEAM, PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

        if (specific.tab()) builder.add(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        if (specific.titles()) builder.addAll(Versions.isCavesAndCliffs() ? CAVES_AND_CLIFFS_TITLE : LEGACY_TITLE);

        return builder.build();

    }

    interface Specific {
        @ConfComments("Обрабатывать предметы и книги?")
        @ConfKey("items")
        @SubSection Items items();

        interface Items {
            @ConfComments("Обрабатывать предметы и книги?")
            @ConfKey("enabled")
            @DefaultBoolean(true)
            boolean enabled();

            @ConfComments({
                    "Отключает декорацию italic в названии и лоре предмета.",
                    "Не знаю как вам, но меня это подбешивает :)",
                    "P.S если вам оно нужно - можете просто добавить тег <i>, эта функция лишь отключает дефолтный курсив ванильного майнкрафта."})
            @ConfKey("disable-italic")
            @DefaultBoolean(false)
            boolean disableItalic();

        }

        @ConfComments("Обрабатывать сообщения при кике / дисконнекте?")
        @ConfKey("kick-disconnect")
        @DefaultBoolean(true)
        boolean kickAndDisconnect();

        @ConfComments("Обрабатывать боссбар?")
        @ConfKey("bossbar")
        @DefaultBoolean(true)
        boolean bossBar();

        @ConfComments("Обрабатывать все сообщения в чате?")
        @ConfKey("chat")
        @DefaultBoolean(true)
        boolean chat();

        @ConfComments("Обрабатывать названия инвентарей?")
        @ConfKey("inventory-titles")
        @DefaultBoolean(true)
        boolean inventoryTitles();

        @ConfComments("Обрабатывать таблицу счета?")
        @ConfKey("scoreboard")
        @DefaultBoolean(true)
        boolean scoreboard();

        @ConfComments("Обрабатывать таб?")
        @ConfKey("tab")
        @DefaultBoolean(true)
        boolean tab();

        @ConfComments("Обрабатывать имена энтити?")
        @ConfKey("entities")
        @DefaultBoolean(true)
        boolean entities();

        @ConfComments("Обрабатывать иконки на картах?")
        @ConfKey("map-icons")
        @DefaultBoolean(true)
        boolean mapIcons();

        @ConfComments("Обрабатывать имена игроков?")
        @ConfKey("player-info")
        @DefaultBoolean(true)
        boolean playerInfo();

        @ConfComments("Обрабатывать MOTD сервера?")
        @ConfKey("motd")
        @DefaultBoolean(true)
        boolean MoTD();

        @ConfComments("Обрабатывать тайтлы? (/title)")
        @ConfKey("titles")
        @DefaultBoolean(true)
        boolean titles();
    }

    @Order(3)
    @ConfComments({
            "Стратегия обработки. Возможные значения: ALL, REGEX",
            "ALL - все сообщения будут обрабатываться",
            "REGEX - обрабатываться будут только сообщения с префиксом и суффиксом, указанными ниже.",
            "Например - такое: [mm]Hello <red> world[/mm]"})
    @ConfKey("parsing-strategy")
    @DefaultString("REGEX")
    ParsingStrategy parsingStrategy();

    @Order(4)
    @ConfComments("Настройки стратегии REGEX.")
    @SubSection Regex regex();
    interface Regex {
        @ConfComments("Префикс.")
        @DefaultString("[mm]")
        String prefix();
        @ConfComments("Суффикс.")
        @DefaultString("[/mm]")
        String suffix();
    }

    @Order(5)
    @ConfComments("Фильтр чата. Игроки без права mmanywhere.ignore не смогут использовать форматтирование.")
    @ConfKey("filter-chat")
    @SubSection Filter filter();

    interface Filter {

        @ConfComments("Фильтровать книги?")
        @ConfKey("books")
        @DefaultBoolean(true)
        boolean books();

        @ConfComments("Фильтровать чат?")
        @ConfKey("chat")
        @DefaultBoolean(true)
        boolean chat();

        @ConfComments("Фильтровать переименованные в наковальне предметы?")
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
    @ConfComments("Красивый чат для консоли.")
    @ConfKey("pretty-chat")
    @DefaultBoolean(true) boolean prettyChat();


    @Order(7)
    @ConfKey("messages")
    @SubSection Messages messages();
    interface Messages {

        @ConfKey("info")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> v 2.0")
        Message info();

        @ConfKey("no-permission")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> <#e60000>У вас нету права на это!")
        Message noPermission();

        @ConfKey("reloaded")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Плагин успешно перезагружен!")
        Message reloaded();

        @ConfKey("unknown-command")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Неизвестная команда!")
        Message unknownCommand();

    }



}

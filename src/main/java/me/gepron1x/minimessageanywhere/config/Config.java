package me.gepron1x.minimessageanywhere.config;

import com.comphenix.protocol.PacketType;
import net.kyori.adventure.text.Component;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import java.util.Collection;
import java.util.Map;

import static space.arim.dazzleconf.annote.ConfDefault.*;
import static space.arim.dazzleconf.sorter.AnnotationBasedSorter.*;

public interface Config {
    @Order(1)
    @ConfComments({"Пакеты, которые плагин будет обратывать плагин", "Не трогайте, если не знаете что делаете!"})
    @DefaultStrings({
            "KICK_DISCONNECT",
            "BOSS",
            "CHAT",
            "OPEN_WINDOW",
            "COMBAT_EVENT",
            "SCOREBOARD_OBJECTIVE",
            "SCOREBOARD_TEAM",
            "TITLE",
            "PLAYER_LIST_HEADER_FOOTER"
    })
    @ConfKey("packets-to-listen")
    Collection<PacketType> packetsToListen();

    @Order(2)
    @ConfComments("специфичные пакеты, требующие индивидуальной обработки.")
    @SubSection Specific specific();
    interface Specific {
        @ConfComments("Обрабатывать предметы и книги?")
        @ConfKey("items")
        @DefaultBoolean(true) boolean items();

        @ConfComments("Обрабатывать имена энтити?")
        @ConfKey("entities")
        @DefaultBoolean(true) boolean entities();

        @ConfComments("Обрабатывать иконки на картах?")
        @ConfKey("map-icons")
        @DefaultBoolean(true) boolean mapIcons();

        @ConfComments("Обрабатывать имена игроков?")
        @ConfKey("player-info")
        @DefaultBoolean(true) boolean playerInfo();

        @ConfComments("Обрабатывать MOTD сервера?")
        @ConfKey("motd")
        @DefaultBoolean(true) boolean MoTD();
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
    @ConfComments("Фильтр чата. Игроки без права minimessage.ignore не смогут использовать minimessage в чате.")
    @ConfKey("filter-chat")
    @DefaultBoolean(false) boolean filterChat();

    @Order(6)
    @ConfComments("Красивый чат для консоли.")
    @ConfKey("pretty-chat")
    @DefaultBoolean(true) boolean prettyChat();

    @Order(7)
    @ConfComments({"Сделанные для вашего удобства плейсхолдеры. Они будут автоматически заменятся на указанные значения во время обработки.",
            "Например: <hello>: Привет!",
            "PAPI поддерживается."
    })
    @DefaultMap({"hello", "Привет, %player_name%"})
    Map<String, String> placeholders();

    @Order(8)
    @ConfKey("messages")
    @SubSection Messages messages();
    interface Messages {

        @ConfKey("info")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> v 2.0")
        Component info();

        @ConfKey("no-permission")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> <#e60000>У вас нету права на это!")
        Component noPermission();

        @ConfKey("reloaded")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Плагин успешно перезагружен!")
        Component reloaded();

        @ConfKey("unknown-command")
        @DefaultString("<gradient:#0073e6:#003cb3>MiniMessageAnywhere</gradient> <bold>|</bold> Неизвестная команда!")
        Component unknownCommand();

    }



}

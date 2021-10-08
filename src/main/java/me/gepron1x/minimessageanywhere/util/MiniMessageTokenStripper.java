package me.gepron1x.minimessageanywhere.util;

import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MiniMessageTokenStripper {
    private static final String DARK_GREY = "dark_grey", GREY = "grey";
    private static final char HEX = '#';
    private static final int HEX_VALUE_LENGTH = 7;
    private static final Pattern TAG = Pattern.compile("(</?([#:<>_a-zA-Z\\d]+)>)");

    private final ImmutableSet<String> SIMPLE_TOKENS = ImmutableSet.<String>builder()
            .addAll(NamedTextColor.NAMES.keys()).add(DARK_GREY, GREY)
            .add(Tokens.BOLD, Tokens.BOLD_2)
            .add(Tokens.UNDERLINED, Tokens.UNDERLINED_2)
            .add(Tokens.STRIKETHROUGH, Tokens.STRIKETHROUGH_2)
            .add(Tokens.OBFUSCATED, Tokens.OBFUSCATED_2)
            .add(Tokens.ITALIC, Tokens.ITALIC_2, Tokens.ITALIC_3).build();

    private final ImmutableSet<String> TOKENS_WITH_PARAMS = ImmutableSet.<String>builder()
            .add(Tokens.CLICK, Tokens.HOVER, Tokens.KEYBIND)
            .add(Tokens.TRANSLATABLE, Tokens.TRANSLATABLE_2, Tokens.TRANSLATABLE_3)
            .add(Tokens.COLOR, Tokens.COLOR_2, Tokens.COLOR_3)
            .add(Tokens.FONT)
            .add(Tokens.RAINBOW, Tokens.GRADIENT).build();


    private final MiniMessage miniMessage;
    private final PlainComponentSerializer plain = PlainComponentSerializer.plain();
    private final Pattern parsablePattern;

    public MiniMessageTokenStripper(MiniMessage miniMessage, Pattern parsablePattern) {
        this.miniMessage = miniMessage;
        this.parsablePattern = parsablePattern;
    }


    public String strip(String text) {
        Matcher parsable = parsablePattern.matcher(text);
        while (parsable.find()) {
            String target = parsable.group(1);
            String value = parsable.group(2);
            Matcher tagMatcher = TAG.matcher(value);
            String replacement = target;
            while (tagMatcher.find()) {
                String tag = tagMatcher.group(1);
                String tagValue = tagMatcher.group(2);
                if (isMatching(tagValue)) replacement = StringUtils.replace(target, tag, "<>");
            }
            text = StringUtils.replace(text, target, replacement);

        }
        return text;
    }

    private boolean isMatching(String value) {
        if (value.charAt(0) == HEX && value.length() == HEX_VALUE_LENGTH) return true;
        if (SIMPLE_TOKENS.contains(value)) return true;


        String[] parts = StringUtils.split(value, Tokens.SEPARATOR);
        if (parts.length == 0) return false;
        return TOKENS_WITH_PARAMS.contains(parts[0]);


    }

    public Component strip(Component component) {
        return Component.text(plain.serialize(miniMessage.parse(plain.serialize(component))));
    }


}

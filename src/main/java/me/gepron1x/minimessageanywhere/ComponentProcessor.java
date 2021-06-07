package me.gepron1x.minimessageanywhere;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Pattern;

import static me.gepron1x.minimessageanywhere.util.AdventureComponentConverter.*;


public class ComponentProcessor implements Processor<Component> {
    private final Pattern translatablePattern = Pattern.compile("\\[mm](.+)\\[/mm]");
    private final MiniMessage miniMessage = MiniMessage.get();
    private final TextReplacementConfig textReplacementConfig =
            TextReplacementConfig.builder()
            .match(translatablePattern)
            .replacement((matchResult, builder) -> miniMessage.parse(matchResult.group(1))).build();
    @Override
    public Component handle(Component input) {
        return input.replaceText(textReplacementConfig);
    }
    public WrappedChatComponent handle(WrappedChatComponent input) {
        return fromComponent(handle(fromWrapper(input)));
    }
}

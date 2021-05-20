import me.gepron1x.minimessageanywhere.ComponentProcessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

import java.util.regex.Pattern;

public class F {
    public static void main(String[] args) {
        Pattern translatablePattern = Pattern.compile("\\[mm](.+)\\[/mm]");
        Component text = Component.text("[mm]<red>сынок ебаный[/mm]");
        ComponentProcessor processor = new ComponentProcessor();
        System.out.println(PlainComponentSerializer.plain().serialize(processor.handle(text)));
    }
}

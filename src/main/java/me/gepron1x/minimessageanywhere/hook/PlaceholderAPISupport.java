package me.gepron1x.minimessageanywhere.hook;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@FunctionalInterface
public interface PlaceholderAPISupport {
    PlaceholderAPISupport IDENTITY = (p, t) -> t;
    String applyPlaceholders(@Nullable Player player, @NotNull String text);


}

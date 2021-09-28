package me.gepron1x.minimessageanywhere.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPISupportImpl implements PlaceholderAPISupport {
    @Override
    public String applyPlaceholders(@Nullable Player player, @NotNull String text) {
        return player == null ? text : PlaceholderAPI.setPlaceholders(player, text);
    }
}

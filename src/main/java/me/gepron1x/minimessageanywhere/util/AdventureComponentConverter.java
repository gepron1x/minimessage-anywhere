package me.gepron1x.minimessageanywhere.util;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

/**
 * Utility class for converting between the Adventure API Component and ProtocolLib's wrapper
 * <p>
 * Note: The Adventure API Component is not included in CraftBukkit, Bukkit or Spigot and but is present in PaperMC.
 */
public class AdventureComponentConverter {

    private AdventureComponentConverter() {
    }

    /**
     * Converts a {@link WrappedChatComponent} into a {@link Component}
     * @param wrapper ProtocolLib wrapper
     * @return Component
     */
    public static Component fromWrapper(WrappedChatComponent wrapper) {
        return GsonComponentSerializer.gson().deserialize(wrapper.getJson());
    }

    /**
     * Converts a {@link Component} into a ProtocolLib wrapper
     * @param components Component
     * @return ProtocolLib wrapper
     */
    public static WrappedChatComponent fromComponent(Component component) {
        return WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(component));
    }

}

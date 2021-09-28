package me.gepron1x.minimessageanywhere.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

public class PrettyChatListener implements Listener {
    private final Server server;
    private final ComponentHandler handler;

    public PrettyChatListener(Server server, ComponentHandler handler) {
        this.server = server;
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        ChatRenderer before = event.renderer();
        event.renderer(
                (player, displayName, message, audience) ->
                        handler.handle(audience, before.render(player, displayName, message, audience))
        );

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBroadcast(BroadcastMessageEvent event) {
        event.message(handler.handle(Audience.audience(event.getRecipients()), event.message()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        event.message(handler.handleIfNotNull(server, event.message()));
    }




}

package fr.pandonia.core.listeners.packets;

import fr.pandonia.api.events.packets.PacketReadEvent;
import fr.pandonia.api.events.packets.PacketWriteEvent;
import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PacketListeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        removePlayer(event.getPlayer());
    }
    private void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                PacketReadEvent packetReadEvent = new PacketReadEvent(packet, player);
                Bukkit.getPluginManager().callEvent(packetReadEvent);
                if(packetReadEvent.isCancelled()) return;
                super.channelRead(channelHandlerContext, packetReadEvent.getPacket());
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                PacketWriteEvent packetWriteEvent = new PacketWriteEvent(packet, player);
                Bukkit.getPluginManager().callEvent(packetWriteEvent);
                if(packetWriteEvent.isCancelled()) return;
                super.write(channelHandlerContext, packetWriteEvent.getPacket(), channelPromise);
            }


        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

    }
}

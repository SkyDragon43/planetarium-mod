package com.tangeriness.planetarium.event;

import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.networking.Packets;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ServerEvents {
    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerWorld world = handler.player.getWorld();
            
            PacketByteBuf packet = StarrySkyManager.getOrCreateStarrySky(world).createRevealStarsPacket();
            Planetarium.LOGGER.info("Revealing stars in " + world.getRegistryKey().getValue() + " to " + handler.player.getName().getString());
            sender.sendPacket(Packets.REVEAL_STARS, packet);
        });
    }
}

package com.tangeriness.planetarium.client.networking;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.client.world.starrysky.ClientStarrySky;
import com.tangeriness.planetarium.networking.Packets;
import com.tangeriness.planetarium.world.starrysky.Star;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientPacketHandler {

    private static void handleRevealStars(MinecraftClient minecraftclient1, ClientPlayNetworkHandler clientplaynetworkhandler2, PacketByteBuf packetbytebuf3, PacketSender packetsender4) {
        
        Identifier world = packetbytebuf3.readIdentifier();
        Planetarium.LOGGER.info("RECIEVED STARS FOR: " + world);
        
        int starCount = packetbytebuf3.readInt();
        List<Star> stars = Lists.newArrayList();
        for (int i = 0; i < starCount; i ++) {
            Star star = new Star(0, 0, 0);
            star.readBuf(packetbytebuf3);
            stars.add(star);
        }

        minecraftclient1.execute(() -> {
            if (minecraftclient1.world.getRegistryKey().getValue().compareTo(world) == 0) {
                Planetarium.LOGGER.info("REVEALING STARS...");

                StarrySky starrySky = ((StarrySkyHolder)minecraftclient1.world).getStarrySky();
                starrySky.setStars(stars);
            }
        }); 
    }
    private static void handleUpdateStar(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        Identifier world = buf.readIdentifier();
        Planetarium.LOGGER.info("UPDATING STAR IN: " + world);

        boolean contains = buf.readBoolean();
        if (contains) {
            int starId = buf.readInt();
            buf.retain();
            client.execute(() -> {
                if (client.world.getRegistryKey().getValue().compareTo(world) == 0) {
                    Planetarium.LOGGER.info("REVEALING STARS...");
    
                    StarrySky starrySky = ((StarrySkyHolder)client.world).getStarrySky();
                    Star star = starrySky.getStar(starId);
                    star.readBuf(buf);

                    Planetarium.LOGGER.info("UPDATED STAR \"" + star.name + "\"");
                }
                buf.release();
            });

        }
    }

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.REVEAL_STARS, ClientPacketHandler::handleRevealStars);
        ClientPlayNetworking.registerGlobalReceiver(Packets.UPDATE_STAR, ClientPacketHandler::handleUpdateStar);
    }

    
}

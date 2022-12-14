package com.tangeriness.planetarium.networking;

import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.init.PSounds;
import com.tangeriness.planetarium.init.SSItems;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ServerPacketHandler {


    public static void handleAdjustSpyglass(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        double amount = buf.readDouble();
        server.execute(() -> {
            if (player != null) {
                if (player.isUsingSpyglass()) {
                    ItemStack activeItem = player.getActiveItem();
                    if (activeItem.isOf(SSItems.SPYGLASS)) {
                        float ogZoom = SSItems.SPYGLASS.getZoom(activeItem);
                        float newZoom = ogZoom + ogZoom/(float)-amount/4;
                        boolean maxed = newZoom < 0.01 || newZoom > 0.6;
                        SSItems.SPYGLASS.setZoom(activeItem, Math.min(Math.max(newZoom,0.01f),0.6f));
                        if (maxed) {
                            player.world.playSound(
                                null, // Player - if non-null, will play sound for every nearby player *except* the specified player
                                player.getBlockPos(), // The position of where the sound will come from
                                SoundEvents.BLOCK_LEVER_CLICK, // The sound that will play
                                SoundCategory.PLAYERS, // This determines which of the volume sliders affect this sound
                                0.8f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
                                2f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
                            );
                        } else {
                            player.world.playSound(
                                null, // Player - if non-null, will play sound for every nearby player *except* the specified player
                                player.getBlockPos(), // The position of where the sound will come from
                                PSounds.SPYGLASS_ADJUST, // The sound that will play
                                SoundCategory.PLAYERS, // This determines which of the volume sliders affect this sound
                                1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
                                1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
                            );
                        }
                        
                        // player.playSound(PSounds.SPYGLASS_ADJUST, 1.0f, 1.0f);
                    }
                }
            }
        });
    }
    
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.ADJUST_SPYGLASS, ServerPacketHandler::handleAdjustSpyglass);
    }
}

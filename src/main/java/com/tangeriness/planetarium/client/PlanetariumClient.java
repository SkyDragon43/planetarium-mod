package com.tangeriness.planetarium.client;

import com.tangeriness.planetarium.client.event.ClientEvents;
import com.tangeriness.planetarium.client.networking.ClientPacketHandler;
import com.tangeriness.planetarium.client.render.sky.StarrySkyRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.ClickEvent;

@Environment(EnvType.CLIENT)
public class PlanetariumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.registerEvents();
        ClientPacketHandler.registerPackets();
    }
    
}

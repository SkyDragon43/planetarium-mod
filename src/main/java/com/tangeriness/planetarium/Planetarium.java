package com.tangeriness.planetarium;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangeriness.planetarium.event.ServerEvents;
import com.tangeriness.planetarium.init.PCommands;
import com.tangeriness.planetarium.init.PSounds;
import com.tangeriness.planetarium.init.SSBlocks;
import com.tangeriness.planetarium.init.SSItems;
import com.tangeriness.planetarium.networking.ServerPacketHandler;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;


public class Planetarium implements ModInitializer{
    public static final String MOD_ID = "planetarium";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {

        SSBlocks.register();
        SSItems.register();
        PSounds.register();
        
        PCommands.registerCommands();
        ServerPacketHandler.registerPackets();
        ServerEvents.registerEvents();

        LOGGER.info(MOD_ID + " INITEITLIZEDDSD SUCCESS!!!!!!!!!!!!!!!!!!");







        ServerWorldEvents.LOAD.register((server, world) -> {
            StarrySkyManager starrysky = StarrySkyManager.getOrCreateStarrySky(world);
            starrysky.Load();
        });
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}

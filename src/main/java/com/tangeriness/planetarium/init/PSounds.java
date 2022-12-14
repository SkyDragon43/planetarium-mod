package com.tangeriness.planetarium.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tangeriness.planetarium.Planetarium;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class PSounds {
    public static final Map<Identifier, SoundEvent> SOUNDS = new HashMap<>();

    //public static final SSWaterBlock OCEAN_WATER = register("ocean_water",new SSWaterBlock(SSFluids.OCEAN_WATER, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0f).dropsNothing()));
    public static final SoundEvent SPYGLASS_ADJUST = register("item.spyglass.adjust");

    private static SoundEvent register(String path) {
        Identifier i = new Identifier(Planetarium.MOD_ID, path);
        SoundEvent sound = SoundEvent.of(i);
        SOUNDS.put(i, sound);
        return sound;
    }
    public static void register() {
        for (Entry<Identifier, SoundEvent> sounds : SOUNDS.entrySet()) {
			Registry.register(Registries.SOUND_EVENT, sounds.getKey(), sounds.getValue());
		}
    }

    
}

package com.tangeriness.planetarium.world.starrysky;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixer;
import com.tangeriness.planetarium.Planetarium;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class StarrySkyManager extends PersistentState{
    public static final String STARRY_SKY = Planetarium.MOD_ID+":"+"starry_sky";

    private final ServerWorld world;

    private final List<Star> stars = Lists.newArrayList();

    public StarrySkyManager(ServerWorld world) {
        this.world = world;
        this.setDirty(true);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        NbtList nbtList = new NbtList();
        for (Star star : this.stars) {
            NbtCompound nbtCompound = new NbtCompound();
            star.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        nbt.put("Stars", nbtList);
        
        return nbt;
    }

    public static String nameFor(RegistryEntry<DimensionType> dimensionTypeEntry) {
        return STARRY_SKY;
    }


    public static StarrySkyManager fromNbt(ServerWorld world, NbtCompound nbt) {
        StarrySkyManager starManager = new StarrySkyManager(world);
        // raidManager.nextAvailableId = nbt.getInt("NextAvailableID");
        // raidManager.currentTime = nbt.getInt("Tick");
        NbtList nbtList = nbt.getList("Stars", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Star star = Star.fromNbt(nbtCompound);
            starManager.stars.add(star);
        }
        return starManager;
    }

    public static StarrySkyManager getOrCreateStarrySky(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(nbt -> StarrySkyManager.fromNbt(world, nbt), () -> new StarrySkyManager(world), StarrySkyManager.nameFor(world.getDimensionEntry()));
    }
}
